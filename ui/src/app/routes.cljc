(ns app.routes)

(def routes {"registration" {:. :app.login.model/reg-page}
             "login"        {:. :app.login.model/login-page}
             "profile"      {:. :app.profile.model/index}
             "schedule"     {:.       :app.schedule.model/index
                             "create" {:. :app.schedule.crud.model/create}
                             [:id]    {:.     :app.schedule.crud.model/show
                                       "show" {:. :app.schedule.show.model/show}}}
             "groups"       {:.       :app.groups.model/index
                             "create" {:. :app.groups.crud.model/create}
                             [:id]    {:. :app.groups.crud.model/show}}})
