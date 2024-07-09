package controllers;

import models.User;
import play.i18n.MessagesApi;
import play.mvc.*;
import play.twirl.api.Html;

import javax.inject.Inject;

public class DashboardController extends Controller {
    private final MessagesApi messagesApi;

    @Inject
    public DashboardController(MessagesApi messagesApi) {
        this.messagesApi = messagesApi;
    }

    public Result dashboard(Http.Request request) {
        String userId = request.session().getOptional("user").orElse(null);
        if (userId == null) {
            return redirect(routes.HomeController.index());
        }
        User user = User.find.byId(Integer.valueOf(userId));
        if (user == null) {
            return redirect(routes.HomeController.index());
        }
        Html dashboardPage = views.html.dashboard.render(userId, request, messagesApi.preferred(request));
        return ok(dashboardPage);
    }

}
