package controllers.admin;

import static com.google.common.base.Preconditions.checkNotNull;
import static j2html.TagCreator.*;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import controllers.AssetsFinder;
import j2html.tags.Tag;
import java.util.concurrent.CompletionStage;
import java.util.Locale;
import java.util.Optional;
import javax.inject.Inject;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.filters.csrf.AddCSRFToken;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.twirl.api.Html;
import services.question.*;
import views.html.admin.questions_view;

/**
 * This controller contains action to handle HTTP requests related to questions:
 *
 * <pre>
 * GET Actions:
 * /questions/new
 *    Shows empty form for creating a new question
 * /questions/edit/{pathString} (eg. /questions/edit/applicant.name)
 *    Shows form with question data and allows admin to edit it
 * /questions
 *    Lists all questions
 *
 * POST       /questions/create
 * PATCH      /questions/edit/pathString
 *
 * GET /questions/view/{pathString} (eg. /questions/view/applicant.name)
 *    Utility function to show a rendered view of the question.
 * </pre>
 */
public class QuestionController extends Controller {
  private QuestionService service = new FakeQuestionServiceImpl();

  private final FormFactory formFactory;
  private final AssetsFinder assetsFinder;
  private final QuestionRenderer renderer;

  @Inject
  public QuestionController(
      AssetsFinder assetsFinder, FormFactory formFactory, QuestionRenderer renderer) {
    this.assetsFinder = assetsFinder;
    this.formFactory = formFactory;
    this.renderer = checkNotNull(renderer);
  }

  /** An action that renders an HTML page for creating a new question. */
  @AddCSRFToken
  public Result newQuestion() {
    return ok(
        questions_view.render(
            "New Question", Html.apply(renderer.buildQuestionForm().render()), assetsFinder));
  }

  /** An action that renders an HTML page for an existiing question. */
  public Result edit(String pathString) {
    QuestionDefinition definition = null;
    try {
      definition = this.service.getQuestionDefinition(pathString);
    } catch (Exception e) {
      // handle invalid quetion path instead of just showing the new question view.
      return newQuestion();
    }
    return ok(
        questions_view.render(
            "Edit Question: " + pathString,
            Html.apply(renderer.buildQuestionForm(definition).render()),
            assetsFinder));
  }

  /** An action that renders an HTML page showing a list of questions. */
  public Result list() {
    Tag contentBody =
        div(
            this.buildAllQuestionsTable(),
            br(),
            this.buildQuestionsSummary(),
            QuestionRenderer.getNewQuestionLink());
    return ok(
        questions_view.render("Questions List", Html.apply(contentBody.render()), assetsFinder));
  }

  /** Writes new QuestionDefinition to the database */
  public Result create(Http.Request request) {
    // THIS IS ALL REUSABLE WITH EDIT
    DynamicForm requestData = formFactory.form().bindFromRequest(request);
    ImmutableMap<Locale, String> questionText =
        ImmutableMap.of(Locale.ENGLISH, requestData.get("questionText"));
    String helpText = requestData.get("questionHelpText").trim();
    Optional<ImmutableMap<Locale, String>> questionHelpText =
        (helpText.length() != 0)
            ? Optional.of(ImmutableMap.of(Locale.ENGLISH, helpText))
            : Optional.empty();
    Optional<QuestionType> questionType = Optional.empty();
    Optional<QuestionDefinition> result =
        service.build(
            requestData.get("questionName"),
            requestData.get("questionPath"),
            requestData.get("questionDescription"),
            questionText,
            questionHelpText,
            questionType);
    // END REUSABLE WITH EDIT
    if (result.isPresent()) {
      service.create(result.get());
    }
    return redirect("/admin/questions");
  }

  /** Writes new QuestionDefinition to the database */
  public Result update(Http.Request request) {
    DynamicForm requestData = formFactory.form().bindFromRequest(request);
    return redirect("/admin/questions");
  }

  private Tag buildAllQuestionsTable() {
    ImmutableList<QuestionDefinition> questions = this.service.getAllQuestions();
    return table(
        QuestionRenderer.buildTableHeader(),
        tbody(each(questions, question -> this.renderer.buildQuestionTableRow(question))));
  }

  private Tag buildQuestionsSummary() {
    ImmutableList<QuestionDefinition> questions = this.service.getAllQuestions();
    return div("Total Questions: " + questions.size());
  }
}
