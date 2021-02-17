package services.question;

public enum QuestionType {
  ADDRESS("Address"),
  NAME("Name"),
  TEXT("Text");

  private final String label;

  private QuestionType(String label) {
    this.label = label;
  }

  public String toString() {
    return this.label;
  }
}
