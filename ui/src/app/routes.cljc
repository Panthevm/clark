(ns app.routes)

(def routes {:.         :app.index.model/index
             "post"     {:.    :app.post.model/index
                         "create" {:. :app.post.model/create}
                         [:id] {:.        :app.post.model/show
                                "edit" {:. :app.post.model/edit}
                                "payment" {:. :app.post.model/payment}}}})
