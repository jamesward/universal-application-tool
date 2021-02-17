package controllers.admin;

import static com.google.common.base.Preconditions.checkNotNull;
import static j2html.TagCreator.*;

import j2html.tags.ContainerTag;
import j2html.tags.DomContent;
import j2html.tags.Tag;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import services.question.*;

/** This controller contains rendering for QuestionDefinitions */
public class QuestionRenderer {
  private final String BASE_URL = "/admin/questions";
  private final String RENDER_URL = "/render/question/";

  @Inject
  public QuestionRenderer() {}

  /** Builds a table header for question definitions. */
  public static Tag buildTableHeader() {
    List<String> headerCells =
        List.of(
            "Path",
            "Id",
            "Version",
            "Name",
            "Description",
            "Question Text",
            "Question Help Text",
            "Question Type",
            "Actions");
    return thead(each(headerCells, headerCell -> th(headerCell)));
  }

  public static Tag getNewQuestionLink() {
    return a("Create a new question").withHref("/admin/questions/new");
  }

  /** Builds the basic form for editing data for a question definition. */
  public Tag buildQuestionForm() {
    return this.buildQuestionForm(null);
  }

  public Tag buildQuestionForm(QuestionDefinition definition) {
    String buttonText = "";

    ContainerTag formTag = form().withMethod("POST");
    if (definition != null) { // Editing a question.
      buttonText = "Update";
      formTag.withAction(BASE_URL + "/update");
      formTag.with(
          label("id: " + definition.getId()),
          br(),
          label("path: " + definition.getPath()),
          br(),
          label("version: " + definition.getVersion()),
          br(),
          br());
    } else {
      buttonText = "Create";
      formTag.withAction(BASE_URL + "/new");
    }
    formTag.with(
        this.inputWithLabel(
            "Name: ",
            "questionName",
            Optional.ofNullable(definition == null ? null : definition.getName())));
    formTag.with(
        this.inputWithLabel(
            "Description: ",
            "questionDescription",
            Optional.ofNullable(definition == null ? null : definition.getDescription())));
    if (definition == null) {
      formTag.with(this.inputWithLabel("Path: ", "questionPath", Optional.empty()));
    }
    formTag.with(
        this.textAreaWithLabel(
            "Question Text: ",
            "questionText",
            Optional.ofNullable(definition == null ? null : definition.getDefaultQuestionText())));
    formTag.with(
        this.textAreaWithLabel(
            "Question Help Text: ",
            "questionHelpText",
            Optional.ofNullable(
                definition == null ? null : definition.getDefaultQuestionHelpText())));
    formTag.with(
        this.formQuestionTypeSelect(
            definition == null ? QuestionType.TEXT : definition.getQuestionType()));
    formTag.with(input().withType("submit").withValue(buttonText));
    return formTag;
  }

  public List<DomContent> inputWithLabel(
      String labelValue, String inputId, Optional<String> value) {
    Tag labelTag = label(labelValue).attr("for", inputId);
    Tag inputTag = input().withType("text").withId(inputId).withName(inputId);
    if (value.isPresent()) {
      inputTag.withValue(value.get());
    }
    return List.of(labelTag, br(), inputTag, br(), br());
  }

  public List<DomContent> textAreaWithLabel(
      String labelValue, String inputId, Optional<String> value) {
    Tag labelTag = label(labelValue).attr("for", inputId);
    Tag textAreaTag = textarea().withType("text").withId(inputId).withName(inputId);
    if (value.isPresent()) {
      textAreaTag.withValue(value.get());
    }
    return List.of(labelTag, br(), textAreaTag, br(), br());
  }

  public List<DomContent> formQuestionTypeSelect(QuestionType selectedType) {
    QuestionType[] questionTypes = QuestionType.values();
    String[] labels =
        Arrays.stream(questionTypes).map(item -> item.toString()).toArray(String[]::new);
    String[] values = Arrays.stream(questionTypes).map(item -> item.name()).toArray(String[]::new);
    return this.formSelect("Question type: ", "questionType", labels, values, selectedType.name());
  }

  public List<DomContent> formSelect(
      String labelValue,
      String selectId,
      String[] optionLabels,
      String[] optionValues,
      String selectedValue) {
    Tag labelTag = label(labelValue).attr("for", selectId);
    ContainerTag selectTag = select().withId(selectId).withName(selectId);
    for (int i = 0; i < optionLabels.length && i < optionValues.length; i++) {
      Tag optionTag = option(optionLabels[i]).withValue(optionValues[i]);
      if (optionValues[i].equals(selectedValue)) {
        optionTag.attr("selected");
      }
      selectTag.with(optionTag);
    }
    return List.of(labelTag, br(), selectTag, br(), br());
  }

  /** Display this as a table row with all fields. */
  public Tag buildQuestionTableRow(QuestionDefinition definition) {
    return tr(
        td(definition.getPath()),
        td("" + definition.getId()),
        td(definition.getVersion()),
        td(definition.getName()),
        td(definition.getDescription()),
        td(definition.getDefaultQuestionText()),
        td(definition.getDefaultQuestionHelpText()),
        td(definition.getQuestionType().toString()),
        td(
            span("view"),
            span(" | "),
            a("edit").withHref(BASE_URL + "/edit/" + definition.getPath())));
  }

  /**
   * This is the view that will be displayed to the end user. This needs to not be type agnostic but
   * I'm not sure how best to do that.
   *
   * <p>I don't want to have to call specific question renderers, but I also don't want a giant
   * switch statement here.
   */
  public Tag renderQuestion(QuestionDefinition definition) {
    switch (definition.getQuestionType()) {
      case ADDRESS:
        return div(div("Street"), div("Unit Number"), div("City"), div("State"), div("Zip Code"));
      case NAME:
        return div(
            div("Title"), div("First Name"), div("Middle Name"), div("Last Name"), div("Suffix"));
      case TEXT:
        return div();
    }
    return div();
  }
}
