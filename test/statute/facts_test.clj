(ns statute.facts-test
  (:require [clojure.string :as str]
            [clojure.test :refer [deftest is]]
            [statute.facts :as facts]))

(deftest gnq-has-spec-basis
  (let [sb (facts/spec-basis "GNQ")]
    (is (= 1 (count sb)))
    (is (every? #(str/starts-with? (:statute/url %) "https://") sb))
    (is (every? :statute/law-number sb))))

(deftest unknown-jurisdiction-has-no-spec-basis
  (is (nil? (facts/spec-basis "ATL")))
  (is (nil? (facts/spec-basis "ZZZ"))))

(deftest coverage-is-honest
  (let [c (facts/coverage ["GNQ" "JPN" "ATL"])]
    (is (= 3 (:requested c)))
    (is (= 1 (:covered c)))
    (is (= ["ATL" "JPN"] (:missing-jurisdictions c)))))

(deftest by-topic-filters
  (is (= ["gnq.ohada-auscgie"]
         (mapv :statute/id (facts/by-topic "GNQ" :corporate-governance))))
  (is (empty? (facts/by-topic "GNQ" :labor)))
  (is (empty? (facts/by-topic "ATL" :corporate-governance))))
