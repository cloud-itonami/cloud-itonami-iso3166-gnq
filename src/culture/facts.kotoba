(ns culture.facts
  "Country-level regional-culture catalog for Equatorial Guinea (GNQ) --
  national dishes, protected products, beverages, crafts, festivals and
  heritage sites, per ADR-2607171400 addendum 2 (cloud-itonami-
  municipality-culture-catalog Wave 1, in com-junkawasaki/root). Sibling
  namespace to `marketentry.facts` / `statute.facts` (ADR-2607141700);
  city-level counterparts live in the cloud-itonami-municipality-* repos.

  Catalog is keyed by UPPERCASE ISO3 (mirrors `statute.facts`); entries
  carry no :culture/municipality (that attribute is city-level only).

  Every entry cites a source URL that was actually fetched and read on
  :culture/retrieved-at -- never fabricated. Summaries state only what the
  cited source confirms. An item not in this table has NO spec-basis, full
  stop; extend `catalog`, do not invent an id/url.")

(def catalog
  "iso3 -> vector of culture entries."
  {"GNQ"
   [{:culture/id "gnq.dish.pepesup"
     :culture/name "Pepesup"
     :culture/country "GNQ"
     :culture/kind :dish
     :culture/summary "Spicy fish soup, the signature dish of Equatorial Guinea."
     :culture/url "https://en.wikipedia.org/wiki/Cuisine_of_Equatorial_Guinea"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "gnq.dish.bambucha"
     :culture/name "Bambucha"
     :culture/country "GNQ"
     :culture/kind :dish
     :culture/summary "Equatoguinean dish of tender cassava leaves crushed and boiled with spices and juice of palm kernels, typically served with plantain or yucca."
     :culture/url "https://en.wikipedia.org/wiki/Cuisine_of_Equatorial_Guinea"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "gnq.dish.sopa-de-pescado-con-cacahuete"
     :culture/name "Sopa de pescado con cacahuete"
     :culture/country "GNQ"
     :culture/kind :dish
     :culture/summary "Equatoguinean soup combining peanuts, fish, onions and tomato."
     :culture/url "https://en.wikipedia.org/wiki/Cuisine_of_Equatorial_Guinea"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "gnq.dish.akwadu"
     :culture/name "Akwadu"
     :culture/country "GNQ"
     :culture/kind :dish
     :culture/summary "Equatoguinean dessert of baked bananas with coconut."
     :culture/url "https://en.wikipedia.org/wiki/Cuisine_of_Equatorial_Guinea"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "gnq.beverage.tope"
     :culture/name "Topé"
     :culture/country "GNQ"
     :culture/kind :beverage
     :culture/summary "Palm wine, a traditional beverage of Equatorial Guinea."
     :culture/url "https://en.wikipedia.org/wiki/Cuisine_of_Equatorial_Guinea"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "gnq.beverage.malamba"
     :culture/name "Malamba"
     :culture/country "GNQ"
     :culture/kind :beverage
     :culture/summary "Local alcoholic beverage of Equatorial Guinea."
     :culture/url "https://en.wikipedia.org/wiki/Cuisine_of_Equatorial_Guinea"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "gnq.festival.abira"
     :culture/name "Abira"
     :culture/country "GNQ"
     :culture/kind :festival
     :culture/summary "Celebration in Equatorial Guinea believed to cleanse the community of evil."
     :culture/url "https://en.wikipedia.org/wiki/Culture_of_Equatorial_Guinea"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "gnq.craft.mvet"
     :culture/name "Mvet"
     :culture/country "GNQ"
     :culture/kind :craft
     :culture/summary "Stringed instrument played by the Fang people of Equatorial Guinea, resembling a cross between a zither and a harp, with up to fifteen strings."
     :culture/url "https://en.wikipedia.org/wiki/Culture_of_Equatorial_Guinea"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}]})

(defn spec-basis [iso3] (get catalog iso3))

(defn coverage
  ([] (coverage (keys catalog)))
  ([iso3s]
   (let [have (filter catalog iso3s)
         missing (remove catalog iso3s)]
     {:requested (count iso3s)
      :covered (count have)
      :covered-jurisdictions (vec (sort have))
      :missing-jurisdictions (vec (sort missing))
      :note (str "cloud-itonami-iso3166-gnq culture catalog "
                 "(ADR-2607171400 addendum 2, Wave 1): " (count (get catalog "GNQ"))
                 " GNQ entries, each with a fetched-and-read citation. "
                 "Extend `culture.facts/catalog`, never fabricate an id/url.")})))

(defn by-kind [iso3 kind]
  (filterv #(= (:culture/kind %) kind) (spec-basis iso3)))
