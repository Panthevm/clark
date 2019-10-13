(ns app.routes)

(def routes {:. :app.groups.model/index
             "groups"    {:. :app.groups.model/index
                          [:id] {:. :app.groups.crud.model/index}}})
