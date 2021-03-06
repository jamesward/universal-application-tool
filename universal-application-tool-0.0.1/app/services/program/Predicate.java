package services.program;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

/** String representation of a predicate with variables from the core data model. */
@AutoValue
public abstract class Predicate {
  @JsonCreator
  public static Predicate create(@JsonProperty("expression") String expression) {
    return new AutoValue_Predicate(expression);
  }

  /**
   * JsonPath compatible predicate expression as a string.
   *
   * <p>See <a href=https://github.com/json-path/JsonPath#inline-predicates>JsonPath Predicates</a>
   */
  @JsonProperty("expression")
  public abstract String expression();
}
