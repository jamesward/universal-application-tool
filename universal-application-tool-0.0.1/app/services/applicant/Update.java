package services.applicant;

import com.google.auto.value.AutoValue;

/** Describes an update to an {@link ApplicantData}. */
@AutoValue
public abstract class Update {
  /** Path to the piece of data to update, e.g., "name.first". */
  public abstract String path();

  /** Value to set at path. */
  public abstract String value();

  // Might want the following as well.
  // public abstract UpdateMetadata updateMetadata();
}
