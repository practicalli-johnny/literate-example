(ns user
  (:require [literate.server :as server]
            [literate.client.core :as literate]

            [clojure.java.io :as io]
            [clojure.data.json :as json]))

(comment

  ;; 1. Start server.
  (def stop-server (server/run-server {:port 8118}))


  ;; 2. `l` is your "client".
  (def l (partial literate/view {:url "http://localhost:8118"}))


  ;; 3. Eval the forms below.


  ;; Enjoy!


  ;; -- Vega Lite.

  (l (literate/vega-lite
       {"$schema" "https://vega.github.io/schema/vega-lite/v4.json"
        :description "A simple bar chart with embedded data."
        :data {:values
               [{:a "A" :b 28}
                {:a "B" :b 55}
                {:a "C" :b 43}
                {:a "D" :b 91}
                {:a "E" :b 81}
                {:a "F" :b 53}
                {:a "G" :b 19}
                {:a "H" :b 87}
                {:a "I" :b 52}]}
        :mark "bar"
        :encoding {:x {:field "a"
                       :type "ordinal"}
                   :y {:field "b"
                       :type "quantitative"}}}))

  ;; -- Code.

  (l (literate/code (slurp (io/resource "literate/client/core.clj"))))


  ;; -- Leaflet.

  (l (literate/leaflet {:center [45.505 -0.09]}))

  (def geojson (json/read (io/reader "src/dev/resources/points.geojson")))

  (def sample (update geojson "features" #(take 10 %)))

  (def center (vec (reverse (get-in geojson ["features" 0 "geometry" "coordinates"]))))

  (def leaflet-widget
    (literate/leaflet
      {:style {:height "600px"}
       :center center
       :zoom 10
       :geojson (update geojson "features" #(take 10 %))}))

  (l leaflet-widget)

  ;; De-emphasise the point shadow
  (l (merge leaflet-widget {:widget/geojson (update geojson "features" #(take 1 %))}))


  ;; -- Markdown.

  (l (literate/markdown "**Welcome to Literate**\n\nEval some forms to get started!"))


  ;; -- HTML.

  (l (literate/html
       (rum.server-render/render-static-markup
         [:div.bg-white.p-3
          [:h1.text-6xl "Hello from Hiccup"]
          [:span "Text"]])))


  ;; -- Row.

  (l (literate/row
       {}
       (literate/leaflet {})
       (literate/vega-lite
         {"$schema" "https://vega.github.io/schema/vega-lite/v4.json"
          :description "A simple bar chart with embedded data."
          :data {:url "https://vega.github.io/editor/data/stocks.csv"}
          :transform [{"filter" "datum.symbol==='GOOG'"}]
          :mark "line"
          :encoding {:x {:field "date"
                         :type "temporal"}
                     :y {:field "price"
                         :type "quantitative"}}})))


  ;; -- Column.

  (l (literate/column
       {}
       (literate/leaflet {})
       (literate/vega-lite
         {"$schema" "https://vega.github.io/schema/vega-lite/v4.json"
          :description "A simple bar chart with embedded data."
          :data {:url "https://vega.github.io/editor/data/stocks.csv"}
          :transform [{"filter" "datum.symbol==='GOOG'"}]
          :mark "line"
          :encoding {:x {:field "date"
                         :type "temporal"}
                     :y {:field "price"
                         :type "quantitative"}}})))


  ;; -- Welcome.

  (l (literate/column
       {}
       (literate/hiccup
         [:div.flex.flex-col.space-y-3.bg-white.p-3.font-light
          [:h1.text-3xl
           {:style {:font-family "Cinzel"}}
           "Welcome to Literate"]

          [:p.font-semibold
           "Literate is a Clojure & ClojureScript application which you can use to visualize data."]

          [:p.mt-4
           "This interface that you're looking at it's called a " [:span.font-bold "Widget"]
           ", and you can create one from a Clojure REPL."]

          [:p.mt-2.mb1 "There are a few different types of Widgets that are supported:"]

          [:ul.list-disc.list-inside.ml-2
           [:li "Code"]
           [:li "Markdown"]
           [:li "Hiccup"]
           [:li "Vega Lite"]
           [:li "Leaflet"]
           [:li "Column layout"]
           [:li "Row layout"]]])

       (literate/hiccup
         [:span.p-2.text-lg "Vega Lite Widget"])

       (literate/vega-lite
         {"$schema" "https://vega.github.io/schema/vega-lite/v4.json"
          :description "A simple bar chart with embedded data."
          :data {:url "https://vega.github.io/editor/data/stocks.csv"}
          :transform [{"filter" "datum.symbol==='GOOG'"}]
          :mark "line"
          :encoding {:x {:field "date"
                         :type "temporal"}
                     :y {:field "price"
                         :type "quantitative"}}})

       (literate/hiccup
         [:span.p-2.text-lg "Code Widget"])

       (literate/code (slurp (io/resource "literate/client/core.clj")))

       (literate/hiccup
         [:span.p-2.text-lg "Leaflet Widget"])

       (literate/leaflet {:style {:height "400px"}
                          :center [51.505 -0.09]
                          :zoom 10})))

  )
