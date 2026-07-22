(ns statute.facts
  "General-law compliance catalog for Equatorial Guinea (GNQ) -- extends
  this repo's `marketentry.facts` (business/company-registration market-
  entry only, narrow scope) with a second, orthogonal catalog of statutes
  a company operating in this jurisdiction must generally track for
  compliance. Mirrors cloud-itonami-iso3166-ben/-btn/-cog's `statute.facts`
  (ADR-2607141700, cloud-itonami-compliance-fact-federation).

  Every entry below cites an OFFICIAL supranational-body-hosted URL --
  never fabricated:

  - **Companies/commercial-entity law**: this iteration specifically
    investigated, rather than assumed, whether Equatorial Guinea is
    itself an OHADA member state -- independently confirmed directly on
    OHADA's own 'Les Etats membres de l'OHADA' page
    (`ohada.org/les-etats-membres-de-lohada/`, WebFetch-verified), which
    lists 'Guinée Equatoriale' among the 17 OHADA member states. So, like
    Benin/CAF/COG, company law is governed DIRECTLY by a SUPRANATIONAL
    instrument, the OHADA Acte uniforme relatif au droit des sociétés
    commerciales et du groupement d'intérêt économique (AUSCGIE) -- this
    iteration independently re-fetched OHADA's own page
    (`ohada.org/en/commercial-companies-and-economic-interest-groups/`,
    WebFetch/curl-verified directly, NOT copied from a sibling entry) and
    confirmed, in OHADA's own words: adoption 'January 30, 2014 in
    Ouagadougou (Burkina Faso)', entry into force 'May 5, 2014'. By the
    OHADA Treaty's own direct-applicability principle (Traité de
    Port-Louis Art. 10, the same finding CAF/COG's sibling catalogs
    document), AUSCGIE applies in Equatorial Guinea without any domestic
    transposition act.
  - This iteration separately confirmed, via OHADA's own
    `ohada.org/en/uniform-acts/` page (fetched twice, plus raw-HTML
    re-verification of the exact surrounding text), that OHADA has NO
    adopted uniform act on public procurement, and that 'Harmonization of
    labor law' on that same page is listed only under a 'New normative
    areas' heading (own text: 'Labor law falls within the scope of the
    OHADA business law, in accordance with Article 2 of the founding
    Treaty' -- i.e. an area OHADA COULD harmonize per Decision No.
    0011/2011/CM/OHADA, not an actually adopted Acte Uniforme). This
    means Equatorial Guinea's public-procurement regime AND its labor
    law both remain purely NATIONAL (not OHADA-harmonized) as of this
    iteration's verification -- see `marketentry.facts` for the honest
    disclosure of what could and could not be independently confirmed
    for the national procurement side, and the gap below for labor law.
  - **Labor law**: this iteration looked for Equatorial Guinea's Ley
    Reguladora de las Relaciones Laborales (the domestic labor-code
    citation the task brief itself named as a lead) via the ILO's NATLEX
    national-labor-law database (`natlex.ilo.org` /
    `webapps.ilo.org/dyn/natlex/...`) -- BLOCKED by a Cloudflare bot
    challenge ('Just a moment...' interstitial) on every attempt, which
    this iteration did NOT attempt to bypass or solve (see repo-wide
    safety floor on CAPTCHA/bot-detection circumvention). `droit-afrique.
    com` (the secondary aggregator this task brief flagged as acceptable
    when disclosed) returned HTTP 403 on every path variant tried
    (`/pays/guinee-equatoriale`, `/textes/Guinee_equatoriale.html`), both
    via curl and WebFetch -- a genuine server-side block, not a fabricated
    excuse. No Equatorial Guinea government site with a working legal-text
    archive could be found (see `marketentry.facts` namespace docstring
    for the full list of dead/parked/blocked domains checked: `anif.gq`
    is real but is the AML/financial-intelligence agency, not a legal
    archive; `inege.gq` is squatted with unrelated content; `presidencia-
    ge.org` is a parked for-sale domain; `equatorialoil.com`, named by
    Wikipedia as the Ministry of Mines, Industry and Energy's site, is
    now a dead/parked domain itself). This iteration is HONESTLY unable
    to cite a labor-code law number/date for Equatorial Guinea this
    session -- no entry is added below rather than inventing one. The
    World Bank's Doing Business 2020 Equatorial Guinea Economy Profile
    (independently fetched and read this session, PDF, `archive.
    doingbusiness.org`) does describe REAL Ministry of Labor registration
    procedures (an inspection book, a labor calendar, a 'Fondo de
    Protección del Trabajador' 1%/0.5% salary contribution) and real
    redundancy-dismissal data, but does not itself name/cite the
    underlying labor-code law number -- so this catalog does not
    construct a `statute.facts` entry from it (that would be inventing a
    citation from a secondary source's uncited factual description,
    exactly the failure mode this fleet's discipline forbids).
  - **Also found, also NOT formalized as a catalog entry (an honest
    scope-narrowing, the same discipline COG applied to Congo's own
    anti-corruption/procurement-penalty laws it found named but did not
    independently fetch as primary text)**: the World Bank's Doing
    Business 2020 report names a 'Law of Fiscal Fees (Ley de Tasas
    Fiscales)' governing official registration/construction fee
    schedules (own text: 'Fees refer to official fees, as published in
    the Law of Fiscal Fees (Ley de Tasas Fiscales). The fee is 3% of the
    value of warehouse construction.') -- named by a secondary source,
    with no law number/date, and no primary text independently fetched
    this iteration. Not added as a formal entry.

  A law not in this table has NO spec-basis, full stop; extend
  `catalog`, do not invent an id/url.")

(def catalog
  "iso3 -> vector of statute entries. `:statute/url` + `:statute/law-number`
  are the citation the governor requires before any compliance-fact
  proposal referencing this law can commit."
  {"GNQ"
   [{:statute/id "gnq.ohada-auscgie"
     :statute/title "Acte uniforme relatif au droit des sociétés commerciales et du groupement d'intérêt économique (AUSCGIE)"
     :statute/jurisdiction "GNQ"
     :statute/kind :law
     :statute/law-number "OHADA Uniform Act -- adopted 30 January 2014 (Ouagadougou), entry into force 5 May 2014; directly applicable in Equatorial Guinea as an OHADA member state per Traité de Port-Louis Art. 10, no domestic transposition act required. Its Art. 261 (per the World Bank's own text, see `marketentry.facts`) requires a notice of company incorporation to be published in a legal journal within 15 days of registration -- the OHADA-side grounding this repo's flagship governor check builds on for Equatorial Guinea's own paid-in minimum-capital floor practice."
     :statute/url "https://www.ohada.org/en/commercial-companies-and-economic-interest-groups/"
     :statute/url-provenance :official-ohada-org
     :statute/enacted-date "2014-01-30"
     :statute/retrieved-at "2026-07-22"
     :statute/topic #{:corporate-governance :incorporation}}]})

(defn spec-basis
  "The jurisdiction's statute vector, or nil -- nil means NO spec-basis
  for that jurisdiction yet."
  [iso3]
  (get catalog iso3))

(defn coverage
  "Honest coverage report, same shape/discipline as `marketentry.facts/coverage`:
  never report a missing jurisdiction as covered."
  ([] (coverage (keys catalog)))
  ([iso3s]
   (let [have (filter catalog iso3s)
         missing (remove catalog iso3s)]
     {:requested (count iso3s)
      :covered (count have)
      :covered-jurisdictions (vec (sort have))
      :missing-jurisdictions (vec (sort missing))
      :note (str "cloud-itonami-iso3166-gnq statute.facts Wave 0 (ADR-2607141700): "
                 (count (get catalog "GNQ")) " GNQ statute(s) seeded with an "
                 "official citation -- a deliberately SMALL catalog (labor "
                 "code and public-procurement law could not be independently "
                 "verified this iteration, see namespace docstring). Extend "
                 "`statute.facts/catalog`, never fabricate a law-id or URL.")})))

(defn by-topic
  "Statutes for `iso3` tagged with `topic` (e.g. :labor, :corporate-governance)."
  [iso3 topic]
  (filterv #(contains? (:statute/topic %) topic) (spec-basis iso3)))
