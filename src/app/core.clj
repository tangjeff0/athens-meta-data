(ns app.core
  (:require [cheshire.core :as cheshire]
            [clojure.java.io :as io]
            [clojure.string :as str])
  (:gen-class))


(def base "athens-metadata-current")

(defn file-data
  [dir-name]
  (let [dir (str base "/" dir-name)]
    (->> dir
      io/file
      .list
      seq
      sort
      (map #(str dir "/" %))
      (map #(-> (slurp %)
                (cheshire/parse-string true))))))


(->> (take 10000 (file-data "issues"))
  (map (fn [{:keys [title body comments html_url] {:keys [login]} :user}]
         {:title title
          ;;:html_url html_url
          :user login
          ;;:body body
          :comments (map (fn [{:keys [body] {:keys [login]} :user}]
                           {:user login})
                            ;;:body body})
                      comments)}))
  (group-by :user)
  (map (fn [[k v]]
         [k (count v)]))
  (sort-by second))


(->> (file-data "reviews_comments")
  (map (fn [x]
         [(-> x :user :login)
          (-> x :body)]))
  (group-by first)
  (map (fn [[k v]]
         [k (count v)]))
  (sort-by second))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
