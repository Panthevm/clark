(ns app.routes)

(def routes {:. :app.index.model/index
             "locations" {:. :app.location.model/index
                          [:id] {:. :app.location.crud.model/index}}
             "faculties" {:. :app.faculty.model/index
                          [:id] {:. :app.faculty.crud.model/index}}
             "groups"    {:. :app.groups.model/index
                          [:id] {:. :app.groups.crud.model/index}}})
