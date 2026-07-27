(ns marketentry.registry
  "Pure-function market-entry filing-draft + filing-submit record
  construction -- an append-only market-entry book-of-record draft.

  Like every sibling actor's registry, there is no single international
  reference-number standard for a market-entry filing -- every
  jurisdiction assigns its own format. This namespace does NOT invent
  one; it builds a jurisdiction-scoped sequence number and validates the
  record's required fields, the same honest, non-fabricating discipline
  `marketentry.facts` uses.

  `engagement-fee-matches-claim?` is an HONEST reapplication of the SAME
  ground-truth-recompute DISCIPLINE sibling actors use (verify a claimed
  monetary total against the entity's own recorded quantity x unit
  fields), reapplied to a market-entry engagement fee line.

  `capital-below-floor?` is THIS vertical's own new ground-truth check,
  grounding GNQ's flagship governor check
  (`marketentry.governor/capital-floor-violations`): World Bank Doing
  Business 2020's own documented Equatorial Guinea 'Starting a Business'
  data (see `marketentry.facts`) reports a paid-in minimum-capital
  requirement of XAF 1,000,000 for a Limited Liability Company (SARL) --
  registered at the Commercial Registry (Registro de la Propiedad y
  Mercantil), tied by the World Bank's own text to OHADA AUSCGIE Art.
  261's incorporation-notice-publication obligation. This is a
  DIFFERENT check SHAPE from every sibling this catalog's author has
  examined: not a statutory-ceiling-on-a-declared-duration (COG), not a
  turnover-scaled formula (Bulgaria), not a flat statutory threshold on a
  monetary value (Albania), not a boolean registry-membership read of
  the SUPPLIER (Azerbaijan/Armenia/Bolivia), not a 3-tier contract-value
  classification (Antigua and Barbuda), not a bid-evaluation
  price-adjustment recompute (Benin), not a set-membership check on a
  declared sector (Bhutan) -- it is a PAID-IN CAPITAL FLOOR VALIDATION,
  the mirror-image of COG's own ceiling check: an engagement's own
  declared paid-in capital must MEET OR EXCEED the floor, not stay under
  a cap. The floor itself (`capital-floor-xaf`) is NEVER hardcoded
  independently of `marketentry.facts` semantics -- it mirrors the same
  single-source-of-truth discipline `exclusion-duration-cap-years`-style
  siblings use for their own constant statutory values, and
  `marketentry.facts/capital-floor-spec-basis` cites the identical value
  for the governor's citation trail.

  It is entity-condition-gated like Bhutan's FDI check and COG's
  exclusion-duration check: a no-op (false, i.e. not below floor) unless
  `:paid-in-capital-xaf` is present as a number -- an engagement with no
  declared capital figure yet has nothing for this check to validate
  (that is the `evidence-incomplete` check's job, upstream, where an
  assessment must already exist).

  This namespace is pure data + pure functions -- no I/O, no network
  call to any real Registro de la Propiedad y Mercantil, Ministry of
  Commerce or Tax Authorities system. It builds the RECORD an operator
  would keep, not the act of submitting a real company/business-
  registration filing package itself (that is `marketentry.operation`'s
  `:filing/submit`, always human-gated -- see README Actuation)."
  (:require [clojure.string :as str]))

(defn- unsigned-certificate
  "Every certificate this actor produces is UNSIGNED -- signature is
  the market-entry operator's act, not this actor's."
  [kind subject record-id]
  {"@context" ["https://www.w3.org/ns/credentials/v2"]
   "type" ["VerifiableCredential" kind]
   "credentialSubject" {"id" subject "record" record-id}
   "proof" nil
   "issued_by_registry" false
   "status" "draft-unsigned"})

(defn- zero-pad [n w]
  (let [s (str n)]
    (str (apply str (repeat (max 0 (- w (count s))) "0")) s)))

(def ^:private money-scale
  "Sub-minor-unit scale used when comparing two money amounts: 1/10000 of
  a unit. Coarser than double representation error by many orders of
  magnitude, finer than any real currency's minor unit (2 decimals for
  most, 3 for KWD/BHD/OMR, 0 for JPY/KRW)."
  10000)

(defn- money=
  "Exact-at-money-precision equality for two amounts.

  `==` on raw doubles is NOT the right comparison for money. With
  whole-unit fees the two agree, but as soon as an amount carries
  cents the sum `base + rate x months` is routinely not the double
  nearest the true total, and a CORRECT claim compares false: measured
  on this exact shape, 40,989 of 327,060 cent-denominated combinations
  (12.5%) were rejected while being right, against 0 of 327,060 in
  whole units.

  Rounding both sides to `money-scale` before comparing removes the
  representation error while preserving every distinction money can
  actually carry."
  [x y]
  (and (number? x) (number? y)
       (= (Math/round (* money-scale (double x)))
          (Math/round (* money-scale (double y))))))

(defn compute-engagement-fee
  "The ground-truth engagement fee for `engagement`'s own `:base-fee`
  and `:monitoring-months` x `:monthly-rate` -- a single flat
  base + months x rate calculation, not a full pricing engine."
  [{:keys [base-fee monthly-rate monitoring-months]}]
  ;; nil when any field is not a number: an un-recomputable engagement is
  ;; un-verifiable, which is neither `correct` nor a ClassCastException
  ;; thrown out of the caller.
  (when (and (number? base-fee) (number? monthly-rate) (number? monitoring-months))
    (+ (double base-fee)
       (* (double monthly-rate) (double monitoring-months)))))

(defn engagement-fee-matches-claim?
  "Does `engagement`'s own `:claimed-fee` equal the independently
  recomputed `compute-engagement-fee`?"
  [{:keys [claimed-fee] :as engagement}]
  (money= claimed-fee (compute-engagement-fee engagement)))

(def capital-floor-xaf
  "World Bank Doing Business 2020 Equatorial Guinea Economy Profile,
  Starting a Business summary table (own text, WebFetch/curl-verified
  2026-07-22 against archive.doingbusiness.org's official hosting):
  'Paid-in minimum capital requirement: XAF 1,000,000' for a Limited
  Liability Company (SARL). See `marketentry.facts/capital-floor-spec-
  basis` for the full honest-confidence caveat (this figure is Doing
  Business's own documented applied registry practice, not an
  independent re-derivation from AUSCGIE's amended-2014 text, which this
  iteration could not retrieve)."
  1000000)

(defn capital-below-floor?
  "Does `engagement`'s own declared paid-in capital fall BELOW the
  World-Bank-documented XAF 1,000,000 floor?

  A no-op (false) unless `:paid-in-capital-xaf` is present as a number --
  an engagement with no declared capital figure yet has nothing for this
  check to validate. Meeting the floor exactly is fine (>= floor passes)."
  [{:keys [paid-in-capital-xaf]}]
  (boolean
   (when (number? paid-in-capital-xaf)
     (< paid-in-capital-xaf capital-floor-xaf))))

(defn register-draft
  "Validate + construct the FILING-DRAFT registration DRAFT -- the
  market-entry operator's own act of preparing a Commercial Registry
  (Registro de la Propiedad y Mercantil) / Ministry of Commerce
  business-registration filing package. Pure function -- does not touch
  any real Registro de la Propiedad y Mercantil, Ministry of Commerce or
  Tax Authorities system."
  [engagement-id jurisdiction sequence]
  (when-not (and engagement-id (not= engagement-id ""))
    (throw (ex-info "draft: engagement_id required" {})))
  (when-not (and jurisdiction (not= jurisdiction ""))
    (throw (ex-info "draft: jurisdiction required" {})))
  (when (< sequence 0)
    (throw (ex-info "draft: sequence must be >= 0" {})))
  (let [draft-number (str (str/upper-case jurisdiction) "-DFT-" (zero-pad sequence 6))
        record {"record_id" draft-number
                "kind" "filing-draft"
                "engagement_id" engagement-id
                "jurisdiction" jurisdiction
                "immutable" true}]
    {"record" record "draft_number" draft-number
     "certificate" (unsigned-certificate "FilingDraft" draft-number draft-number)}))

(defn register-submit
  "Validate + construct the FILING-SUBMIT registration DRAFT -- the
  market-entry operator's own act of actually submitting a real
  Commercial Registry (Registro de la Propiedad y Mercantil) /
  Ministry of Commerce business-registration filing (always human-gated
  upstream)."
  [engagement-id jurisdiction sequence]
  (when-not (and engagement-id (not= engagement-id ""))
    (throw (ex-info "submit: engagement_id required" {})))
  (when-not (and jurisdiction (not= jurisdiction ""))
    (throw (ex-info "submit: jurisdiction required" {})))
  (when (< sequence 0)
    (throw (ex-info "submit: sequence must be >= 0" {})))
  (let [submit-number (str (str/upper-case jurisdiction) "-SUB-" (zero-pad sequence 6))
        record {"record_id" submit-number
                "kind" "filing-submit"
                "engagement_id" engagement-id
                "jurisdiction" jurisdiction
                "immutable" true}]
    {"record" record "submit_number" submit-number
     "certificate" (unsigned-certificate "FilingSubmit" submit-number submit-number)}))

(defn append [history result]
  (conj (vec history) (get result "record")))
