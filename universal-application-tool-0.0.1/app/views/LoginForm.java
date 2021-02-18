package views;

import static j2html.TagCreator.body;
import static j2html.TagCreator.form;
import static j2html.TagCreator.h1;

import play.mvc.Http;
import play.twirl.api.Content;

public class LoginForm extends BaseHtmlView {

  public Content render(Http.Request request) {
    return htmlContent(
        body(
            h1("Error: You are not logged in")
                .withCondHidden(request.queryString("message").equals("login")),
            h1("Log In"),
            form(
                    makeCsrfTokenInputTag(request),
                    textField("uname", "username", "Username"),
                    passwordField("pwd", "password", "Password"),
                    submitButton("login", "Submit"))
                .withMethod("POST")
                .withAction("/callback?client_name=FormClient"),
            h1("Or, continue as guest."),
            button("guest", "continue")
                .attr("onclick", "window.location = '/callback?client_name=GuestClient';")));
  }
}
