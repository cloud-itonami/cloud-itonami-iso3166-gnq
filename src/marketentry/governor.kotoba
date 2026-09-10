(ns marketentry.governor
  "Market-Entry Compliance Governor -- the independent compliance layer
  that earns the MarketEntry-LLM the right to commit. The LLM has no
  notion of Equatorial Guinea company/business-registration law,
  whether a claimed engagement fee actually equals base + months x
  rate, whether an engagement's own declared paid-in capital actually
  meets the Commercial Registry (Registro de la Propiedad y Mercantil)'s
  own World-Bank-documented XAF 1,000,000 floor, whether a tax
  identification number has been verified for a filing that requires
  one, or when a draft stops being a draft and becomes a real-world
  Registro de la Propiedad y Mercantil / Ministry of Commerce filing, so
  this MUST be a separate system able to *reject* a proposal and fall
  back to HOLD.

  `:itonami.blueprint/governor` is `:market-entry-compliance-governor`
  (shared family keyword on blueprints).

  This blueprint's own text (docs/business-model.md Trust Controls:
  'any actual Commercial Registry / Ministry of Commerce filing
  submission requires Market-Entry Compliance Governor clearance and
  always escalates to human sign-off'; 'a false or fabricated
  regulatory-requirement claim is a HARD hold') names exactly the checks
  below.

  Six checks, in priority order, ALL HARD violations: a human
  approver CANNOT override them. The confidence/actuation gate is
  SOFT: it asks a human to look (low confidence / actuation), and the
  human may approve -- but see `marketentry.phase`: for `:stake
  :actuation/draft-filing`/`:actuation/submit-filing` NO phase ever
  allows auto-commit either. Two independent layers agree that
  actuation is always a human call.

    1. Spec-basis                  -- did the jurisdiction proposal cite
                                       an OFFICIAL source
                                       (`marketentry.facts`), or invent
                                       one?
    2. Evidence incomplete         -- for `:filing/draft`/
                                       `:filing/submit`, has the
                                       jurisdiction actually been
                                       assessed with a full evidence
                                       checklist on file?
    3. Paid-in capital below floor -- for `:filing/submit`,
                                       INDEPENDENTLY recompute whether
                                       the engagement's own declared
                                       `:paid-in-capital-xaf` meets or
                                       exceeds the Commercial Registry's
                                       own World-Bank-documented XAF
                                       1,000,000 floor for a Limited
                                       Liability Company (SARL), and
                                       HARD-hold if it falls short.
                                       FLAGSHIP check for this
                                       jurisdiction -- a PAID-IN CAPITAL
                                       FLOOR VALIDATION (no turnover
                                       formula, no flat monetary
                                       ceiling, no supplier-registry
                                       read, no 3-tier value class, no
                                       bid-evaluation price adjustment,
                                       no sector set-membership, and the
                                       mirror-image of COG's own
                                       exclusion-duration CEILING
                                       check), a check SHAPE genuinely
                                       different from every sibling this
                                       catalog's author has examined.
                                       See `marketentry.facts` /
                                       `marketentry.registry`.
    4. Engagement fee mismatch     -- for `:filing/submit`,
                                       INDEPENDENTLY recompute whether
                                       the engagement's own `:claimed-
                                       fee` equals `base-fee +
                                       monthly-rate x monitoring-
                                       months` -- honest reapplication
                                       of the ground-truth-recompute
                                       discipline sibling actors use.
    5. Tax identification number
       unverified                   -- for `:filing/submit`, when the
                                       engagement declares
                                       `:requires-tax-id? true`,
                                       INDEPENDENTLY check
                                       `:tax-id-verified?`. CONDITIONAL
                                       on the engagement's own ground
                                       truth. Grounded in the Tax
                                       Authorities (Public Treasury)'s
                                       tax identification number (see
                                       `marketentry.facts`).
    6. Confidence floor / actuation
       gate                          -- LLM confidence below threshold,
                                       OR the op is `:filing/draft`/
                                       `:filing/submit` (REAL acts)
                                       -> escalate.

  Two more guards, double-draft/double-submit prevention, are enforced
  off dedicated `:drafted?`/`:submitted?` facts (never a `:status`
  value)."
  (:require [marketentry.facts :as facts]
            [marketentry.registry :as registry]
            [marketentry.store :as store]))

(def confidence-floor 0.6)

(def high-stakes
  "Stakes grave enough to always require a human, even when clean.
  Drafting a real Commercial Registry / Ministry of Commerce filing
  package and submitting it are the two real-world actuation events
  this actor performs."
  #{:actuation/draft-filing :actuation/submit-filing})

;; ----------------------------- checks -----------------------------

(defn- spec-basis-violations
  "A `:jurisdiction/assess` (or `:filing/draft`/`:filing/submit`)
  proposal with no spec-basis citation is a HARD violation -- never
  invent a jurisdiction's market-entry requirements."
  [{:keys [op]} proposal]
  (when (contains? #{:jurisdiction/assess :filing/draft :filing/submit} op)
    (let [value (:value proposal)]
      (when (or (empty? (:cites proposal))
                (and (contains? value :spec-basis) (nil? (:spec-basis value))))
        [{:rule :no-spec-basis
          :detail "公式spec-basisの引用が無い提案は法域要件として扱えない"}]))))

(defn- evidence-incomplete-violations
  "For `:filing/draft`/`:filing/submit`, the jurisdiction's required
  registration evidence must actually be satisfied."
  [{:keys [op subject]} st]
  (when (contains? #{:filing/draft :filing/submit} op)
    (let [e (store/engagement st subject)
          assessment (store/assessment-of st subject)]
      (when-not (and assessment
                     (facts/required-evidence-satisfied?
                      (:jurisdiction e) (:checklist assessment)))
        [{:rule :evidence-incomplete
          :detail "法域の必要書類(商業登記/資本金証明/納税者番号/労働省登録等)が充足していない状態での提案"}]))))

(defn- capital-floor-violations
  "For `:filing/submit`, INDEPENDENTLY recompute whether the
  engagement's own declared paid-in capital stays at or above the
  Commercial Registry's own World-Bank-documented XAF 1,000,000 floor --
  the flagship check this vertical adds. Entity-condition-gated (a
  no-op unless `:paid-in-capital-xaf` is a declared number)."
  [{:keys [op subject]} st]
  (when (= op :filing/submit)
    (let [e (store/engagement st subject)]
      (when (registry/capital-below-floor? e)
        [{:rule :capital-below-floor
          :detail (str subject " の申告払込資本金(XAF " (:paid-in-capital-xaf e)
                      ")がCommercial Registry(Registro de la Propiedad y Mercantil)の"
                      "最低払込資本金基準(XAF " registry/capital-floor-xaf
                      ", World Bank Doing Business 2020)を下回っており、"
                      "提出提案は進められない")}]))))

(defn- engagement-fee-mismatch-violations
  "For `:filing/submit`, INDEPENDENTLY recompute whether the
  engagement's own claimed fee equals base + months x rate."
  [{:keys [op subject]} st]
  (when (= op :filing/submit)
    (let [e (store/engagement st subject)]
      (when-not (registry/engagement-fee-matches-claim? e)
        [{:rule :engagement-fee-mismatch
          :detail (str subject " の申告手数料(" (:claimed-fee e)
                      ")が独立再計算値(" (registry/compute-engagement-fee e) ")と一致しない")}]))))

(defn- tax-id-unverified-violations
  "For `:filing/submit`, when the engagement declares `:requires-tax-id?
  true`, INDEPENDENTLY check `:tax-id-verified?` -- CONDITIONAL on the
  engagement's own ground truth. Grounded in the Tax Authorities
  (Public Treasury)'s tax identification number requirement (World Bank
  Doing Business 2020's own text)."
  [{:keys [op subject]} st]
  (when (= op :filing/submit)
    (let [e (store/engagement st subject)]
      (when (and (true? (:requires-tax-id? e))
                 (not (true? (:tax-id-verified? e))))
        [{:rule :tax-id-unverified
          :detail (str subject " はTax Authorities(Public Treasury)への納税者番号(tax identification number)登録確認を"
                      "要するが未確認 -- 提出提案は進められない")}]))))

(defn- already-drafted-violations
  "For `:filing/draft`, refuses to draft the SAME engagement twice."
  [{:keys [op subject]} st]
  (when (= op :filing/draft)
    (when (store/engagement-already-drafted? st subject)
      [{:rule :already-drafted
        :detail (str subject " は既にドラフト済み")}])))

(defn- already-submitted-violations
  "For `:filing/submit`, refuses to submit the SAME engagement twice."
  [{:keys [op subject]} st]
  (when (= op :filing/submit)
    (when (store/engagement-already-submitted? st subject)
      [{:rule :already-submitted
        :detail (str subject " は既に提出済み")}])))

(defn check
  "Censors a MarketEntry-LLM proposal against the governor rules.
  Returns {:ok? bool :violations [..] :confidence c :escalate? bool
  :high-stakes? bool :hard? bool}."
  [request _context proposal st]
  (let [hard (into []
                   (concat (spec-basis-violations request proposal)
                           (evidence-incomplete-violations request st)
                           (capital-floor-violations request st)
                           (engagement-fee-mismatch-violations request st)
                           (tax-id-unverified-violations request st)
                           (already-drafted-violations request st)
                           (already-submitted-violations request st)))
        conf (:confidence proposal 0.0)
        low? (< conf confidence-floor)
        stakes? (boolean (high-stakes (:stake proposal)))
        hard? (boolean (seq hard))]
    {:ok?          (and (not hard?) (not low?) (not stakes?))
     :violations   hard
     :confidence   conf
     :hard?        hard?
     :escalate?    (and (not hard?) (or low? stakes?))
     :high-stakes? stakes?}))

(defn hold-fact
  "The audit fact written when a proposal is rejected (HOLD)."
  [request context verdict]
  {:t          :governor-hold
   :op         (:op request)
   :actor      (:actor-id context)
   :subject    (:subject request)
   :disposition :hold
   :basis      (mapv :rule (:violations verdict))
   :violations (:violations verdict)
   :confidence (:confidence verdict)})
