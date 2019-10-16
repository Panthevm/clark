(ns app.routes)

(def routes {:.         :app.groups.model/index
             "schedule" {:. :app.schedule.model/index}
             "groups"   {:.    :app.groups.model/index
                         "create" {:. :app.groups.crud.model/create}
                         [:id] {:. :app.groups.crud.model/show}}})
