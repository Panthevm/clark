(ns app.routes)

(def routes {:.         :app.groups.model/index
             "login"             {:. :app.login.model/login-page}
             "schedule" {:.    :app.schedule.model/index
                         "create" {:. :app.schedule.crud.model/create}
                         [:id] {:. :app.schedule.crud.model/show
                                "show" {:. :app.schedule.show.model/show}}}
             "groups"   {:.    :app.groups.model/index
                         "create" {:. :app.groups.crud.model/create}
                         [:id] {:. :app.groups.crud.model/show}}})
