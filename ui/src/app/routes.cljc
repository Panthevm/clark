(ns app.routes)

(def routes {:. :app.index.model/index
             "locations" {:. :app.location.model/index
                          [:id] {:. :app.location.crud.model/index}}
             "groups"    {:. :app.groups.model/index}})
