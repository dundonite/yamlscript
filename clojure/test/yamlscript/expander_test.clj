; Copyright 2023 Ingy dot Net
; This code is licensed under MIT license (See License for details)

(ns yamlscript.expander-test
  (:use yamlscript.debug)
  (:require
   [yamlscript.parser :as parser]
   [yamlscript.composer :as composer]
   [yamlscript.resolver :as resolver]
   [yamlscript.builder :as builder]
   [yamlscript.expander :as expander]
   [yamlscript.test :as test]
   [clojure.edn :as edn]))

(def test-files
  ["test/compiler-stack.yaml"])

(test/remove-tests)

(doseq [test-file test-files]
  (test/load-yaml-tests
   {:yaml-file test-file
    :pick-func #(test/has-keys? [:yamlscript :expand] %)
    :test-func (fn [test]
                 (->> test
                      :yamlscript
                      parser/parse
                      composer/compose
                      resolver/resolve
                      builder/build
                      expander/expand))
    :want-func (fn [test]
                 (->> test
                      :expand
                      edn/read-string))}))
