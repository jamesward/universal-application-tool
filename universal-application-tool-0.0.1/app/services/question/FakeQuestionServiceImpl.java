package services.question;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class FakeQuestionServiceImpl implements QuestionService {
  private ArrayList<QuestionDefinition> definitions;

  public FakeQuestionServiceImpl() {
    this.seedData();
  }

  private void seedData() {
    this.definitions =
        new ArrayList<>(
            List.of(
                new NameQuestionDefinition(
                    1L,
                    "1",
                    "applicant name",
                    "applicant.name",
                    "The name of the applicant",
                    ImmutableMap.of(Locale.ENGLISH, "What is your name?"),
                    Optional.empty()),
                new AddressQuestionDefinition(
                    2L,
                    "1",
                    "applicant addresss",
                    "applicant.address",
                    "The address of the applicant",
                    ImmutableMap.of(Locale.ENGLISH, "What is your address?"),
                    Optional.empty()),
                new QuestionDefinition(
                    3L,
                    "1",
                    "applicant's favorite color",
                    "applicant.favoriteColor",
                    "The favorite color of the applicant",
                    ImmutableMap.of(Locale.ENGLISH, "What is your favorite color?"),
                    Optional.empty()),
                new NameQuestionDefinition(
                    4L,
                    "1",
                    "applicant's spouse's name",
                    "applicant.spouse.name",
                    "The name color of the applicant's spouse",
                    ImmutableMap.of(Locale.ENGLISH, "What is your spouse's name?"),
                    Optional.empty())));
  }

  public long nextId() {
    return definitions.size() + 1;
  }

  /**
   * Creates a new Question Definition. Returns a QuestionDefinition object on success and {@link
   * Optional#empty} on failure.
   *
   * <p>This will fail if he path provided already resolves to a QuestionDefinition or Scalar.
   *
   * <p>NOTE: This does not update the version.
   */
  public Optional<QuestionDefinition> create(QuestionDefinition definition) {
    // validate id is unique.
    // validate path + version is unique (and incrementing).
    this.definitions.add(definition);
    return Optional.of(definition);
  }

  public Optional<QuestionDefinition> build(
      String name,
      String path,
      String description,
      ImmutableMap<Locale, String> questionText,
      Optional<ImmutableMap<Locale, String>> questionHelpText,
      Optional<QuestionType> questionType) {
    long id = this.nextId();
    String latestVersion = this.getLastetVersion(path);
    String version = (Long.parseLong(latestVersion) + 1) + "";
    QuestionType type = questionType.orElse(QuestionType.TEXT);
    switch (type) {
      case ADDRESS:
        return Optional.of(
            new AddressQuestionDefinition(
                id, version, name, path, description, questionText, questionHelpText));
      case NAME:
        return Optional.of(
            new NameQuestionDefinition(
                id, version, name, path, description, questionText, questionHelpText));
      case TEXT:
        return Optional.of(
            new QuestionDefinition(
                id, version, name, path, description, questionText, questionHelpText));
      default:
        return Optional.empty();
    }
  }

  /**
   * Adds a new translation to an existing question definition. Returns true if the write is
   * successful.
   *
   * <p>The write will fail if:
   *
   * <p>- The path does not resolve to a QuestionDefinition.
   *
   * <p>- A translation with that Locale already exists for a given question path.
   *
   * <p>NOTE: This does not update the version.
   */
  public boolean addTranslation(
      String path, Locale Locale, String questionText, Optional<String> questionHelpText)
      throws InvalidPathException {
    throw new InvalidPathException(path + "[Not Implemented]");
  }

  /**
   * Destructive overwrite of a question at a given path.
   *
   * <p>NOTE: This updates the service and question versions.
   */
  public QuestionDefinition update(QuestionDefinition definition) {
    return definition;
  }

  /** Checks whether a specific path is valid. */
  public boolean isValid(String pathString) {
    return false;
  }

  /** Yeah... Version should probably be a long. */
  public String getLastetVersion(String pathString) {
    ArrayList<QuestionDefinition> versions = this.getQuestionDefinitionVersions(pathString);
    String latest = "0";
    for (QuestionDefinition definition : versions) {
      if (latest == null || latest.compareTo(definition.getVersion()) < 0) {
        latest = definition.getVersion();
      }
    }
    return latest;
  }

  private ArrayList<QuestionDefinition> getQuestionDefinitionVersions(String pathString) {
    ArrayList<QuestionDefinition> versions = new ArrayList<QuestionDefinition>();
    for (QuestionDefinition definition : definitions) {
      if (definition.getPath().equals(pathString)) {
        versions.add(definition);
      }
    }
    return versions;
  }

  /**
   * Gets the question definition for a given path.
   *
   * <p>If the path is to a QUESTION, it will return that.
   *
   * <p>If the path is to a SCALAR, it will return the parent QuestionDefinition for that Scalar.
   *
   * <p>If the path is invalid it will throw an InvalidPathException.
   */
  public QuestionDefinition getQuestionDefinition(String pathString) throws InvalidPathException {
    ArrayList<QuestionDefinition> versions = this.getQuestionDefinitionVersions(pathString);
    QuestionDefinition latest = null;
    for (QuestionDefinition definition : versions) {
      if (latest == null || latest.getVersion().compareTo(definition.getVersion()) < 0) {
        latest = definition;
      }
    }
    if (latest != null) {
      return latest;
    }
    throw new InvalidPathException(pathString);
  }

  /**
   * Returns all of the scalar properties for a given path.
   *
   * <p>If the path is to a QUESTION, it will return the question's scalar objects.
   *
   * <p>If the path is to a SCALAR, it will return a single scalar.
   *
   * <p>If the path is invalid it will throw an InvalidPathException.
   */
  public ImmutableMap<String, ScalarType> getPathScalars(String pathString)
      throws InvalidPathException {
    throw new InvalidPathException(pathString + "[Not Implemented]");
  }

  /**
   * Gets the type of the node if it exist.
   *
   * <p>If the path is invalid it will throw an InvalidPathException.
   */
  public PathType getPathType(String pathString) throws InvalidPathException {
    throw new InvalidPathException(pathString + "[Not Implemented]");
  }

  /** Returns all question definitions for this version. */
  public ImmutableList<QuestionDefinition> getAllQuestions() {
    return ImmutableList.copyOf(definitions);
  }
}
