; To inform IntelliJ explicitely about deftask, set-env!, task-options!
(require '[boot.core :refer :all])

(def boot-version
  (get (boot.App/config) "BOOT_VERSION" "2.5.5"))

(defn read-project-clj []
  (let [l (-> "project.clj" slurp read-string)]
    (merge (->> l (drop 3) (partition 2) (map vec) (into {}))
           {:project (second l) :version (nth l 2)})))

(let [lein-proj (read-project-clj)]
  (merge-env! :repositories (:repositories lein-proj))
  (set-env!
    :certificates (:certificates lein-proj)
    :source-paths (or (set (:source-paths lein-proj)) #{"src"})
    :resource-paths (or (set (:resource-paths lein-proj)) #{"resources"})
    :dependencies (into (:dependencies lein-proj)
                        `[[boot/core ~boot-version :scope "provided"]
                          [adzerk/bootlaces "0.1.13" :scope "test"]]))

  (require '[adzerk.bootlaces :refer :all])
  ((resolve 'bootlaces!) (:version lein-proj))
  (task-options!
    repl (:repl-options lein-proj {})
    aot (let [aot (:aot lein-proj)
              all? (or (nil? aot) (= :all aot))
              ns (when-not all? (set aot))]
          {:namespace ns :all all?})
    jar {:main (:main lein-proj)}
    pom {:project     (symbol (:project lein-proj))
         :version     (:version lein-proj)
         :description (:description lein-proj)
         :url         (:url lein-proj)
         :scm         (:scm lein-proj)
         :license     (get lein-proj :license {"EPL" "http://www.eclipse.org/legal/epl-v10.html"})}))

(deftask dev
         "Backend in development mode"
         []
         (require '[learn-datalog :refer :all])
         (comp
           (repl)))
