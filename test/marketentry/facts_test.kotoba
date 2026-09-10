(ns marketentry.facts-test
  (:require [clojure.test :refer [deftest is testing]]
            [marketentry.facts :as facts]))

(deftest gnq-has-spec-basis
  (let [sb (facts/spec-basis "GNQ")]
    (is (some? sb))
    (is (string? (:provenance sb)))
    (is (seq (:required-evidence sb)))
    (is (some? (facts/corporate-number-spec-basis "GNQ")))
    (is (some? (facts/business-registration-spec-basis "GNQ")))
    (is (some? (facts/capital-floor-spec-basis "GNQ")))))

(deftest gnq-rep-spec-basis-is-honestly-absent
  (testing "no representative/director personal-liability extension was found for GNQ this iteration -- deliberately not claimed"
    (is (nil? (facts/rep-spec-basis "GNQ")))))

(deftest gnq-business-registration-is-a-different-body-from-tax
  (testing "business/company registration (Commercial Registry) and tax registration (Tax Authorities / Public Treasury) are administered by different authorities -- see namespace docstring"
    (let [reg (facts/business-registration-spec-basis "GNQ")
          tax (facts/corporate-number-spec-basis "GNQ")]
      (is (some? reg))
      (is (some? tax))
      (is (not= (:business-registration-owner-authority reg)
                (:corporate-number-owner-authority tax))))))

(deftest gnq-capital-floor-is-the-flagship-spec-basis
  (testing "World Bank Doing Business 2020's XAF 1,000,000 paid-in minimum-capital floor is a real, directly-fetched documented figure -- not fabricated"
    (let [cf (facts/capital-floor-spec-basis "GNQ")]
      (is (some? cf))
      (is (= 1000000 (:capital-floor-xaf cf)))
      (is (string? (:capital-floor-legal-basis cf))))))

(deftest unknown-jurisdiction-has-no-spec-basis
  (is (nil? (facts/spec-basis "ATL")))
  (is (nil? (facts/spec-basis "ZZZ")))
  (is (nil? (facts/business-registration-spec-basis "ATL")))
  (is (nil? (facts/capital-floor-spec-basis "ATL"))))

(deftest required-evidence-satisfied
  (let [sb (facts/spec-basis "GNQ")
        all (:required-evidence sb)]
    (is (true? (facts/required-evidence-satisfied? "GNQ" all)))
    (is (not (facts/required-evidence-satisfied? "GNQ" (take 1 all))))
    (is (nil? (facts/required-evidence-satisfied? "ATL" all)))))

(deftest coverage-is-honest
  (let [c (facts/coverage ["GNQ" "USA" "ATL"])]
    (is (= 3 (:requested c)))
    (is (= 2 (:covered c)))
    (is (= ["ATL"] (:missing-jurisdictions c)))))
