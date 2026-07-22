(ns marketentry.registry-test
  (:require [clojure.test :refer [deftest is testing]]
            [marketentry.registry :as registry]))

(deftest engagement-fee-recompute
  (let [e {:base-fee 500000 :monthly-rate 30000 :monitoring-months 12 :claimed-fee 860000.0}]
    (is (== 860000.0 (registry/compute-engagement-fee e)))
    (is (true? (registry/engagement-fee-matches-claim? e))))
  (let [bad {:base-fee 500000 :monthly-rate 30000 :monitoring-months 12 :claimed-fee 999000.0}]
    (is (false? (registry/engagement-fee-matches-claim? bad)))))

(deftest register-draft-and-submit
  (let [d (registry/register-draft "eng-1" "GNQ" 0)
        s (registry/register-submit "eng-1" "GNQ" 0)]
    (is (= "GNQ-DFT-000000" (get d "draft_number")))
    (is (= "GNQ-SUB-000000" (get s "submit_number")))
    (is (nil? (get-in d ["certificate" "proof"])))
    (is (= "draft-unsigned" (get-in s ["certificate" "status"])))))

(deftest register-requires-ids
  (is (thrown? Exception (registry/register-draft "" "GNQ" 0)))
  (is (thrown? Exception (registry/register-submit "eng-1" "" 0))))

(deftest capital-floor-recompute
  (testing "World Bank Doing Business 2020 -- a declared paid-in capital at or above the XAF 1,000,000 floor is fine"
    (is (false? (registry/capital-below-floor? {:paid-in-capital-xaf 1000000})))
    (is (false? (registry/capital-below-floor? {:paid-in-capital-xaf 2000000}))))
  (testing "a declared paid-in capital below the floor is a violation"
    (is (true? (registry/capital-below-floor? {:paid-in-capital-xaf 500000})))
    (is (true? (registry/capital-below-floor? {:paid-in-capital-xaf 999999.99}))))
  (testing "entity-condition-gated: a no-op (false) unless :paid-in-capital-xaf is a declared number"
    (is (false? (registry/capital-below-floor? {})))
    (is (false? (registry/capital-below-floor? {:paid-in-capital-xaf nil})))
    (is (false? (registry/capital-below-floor? {:paid-in-capital-xaf "unknown"})))))
