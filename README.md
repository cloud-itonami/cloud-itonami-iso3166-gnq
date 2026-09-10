# cloud-itonami-iso3166-gnq

**GNQ**: Equatorial Guinea.

- No verifiable national public-procurement law/regulator/portal was
  found this iteration (OHADA and CEMAC both independently confirmed to
  have no procurement-harmonization instrument covering GNQ) -- this
  vertical's market-entry mechanism is honestly PIVOTED to company/
  business registration instead of procurement-bid filing
- OHADA member state -- AUSCGIE company law applies directly (no
  domestic transposition act); Commercial Registry (Registro de la
  Propiedad y Mercantil) performs the RCCM-equivalent registration act,
  tied by World Bank Doing Business 2020 to OHADA AUSCGIE Art. 261
- World Bank Doing Business 2020 XAF 1,000,000 paid-in minimum-capital
  floor (SARL) gate -- this vertical's flagship governor check

AGPL-3.0-or-later.

## Culture catalog

Alongside the market-entry / statute catalogs, this repo carries a
**country-level regional-culture catalog** (ADR-2607171400 addendum 2,
`cloud-itonami-municipality-culture-catalog` Wave 1, in
`com-junkawasaki/root`) — national dishes, protected products, beverages,
crafts, festivals and heritage sites for Equatorial Guinea:

- `src/culture/facts.kotoba` — the catalog, source of truth (keyed by
  uppercase ISO3, mirroring `statute.facts`).
- `schema/culture.edn` — DataScript schema.
- `data/culture-tx.edn` — derived DataScript tx-data (regenerated from
  the catalog, never hand-edited).

City-level counterparts live in the `cloud-itonami-municipality-*` repos.
Same provenance discipline as the compliance catalogs: every entry cites a
source URL that was actually fetched and read on `:culture/retrieved-at`;
summaries state only what the cited source confirms. An item not in
`culture.facts/catalog` has no spec-basis — never fabricate one.
