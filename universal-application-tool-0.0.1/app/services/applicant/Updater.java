package services.applicant;

import com.google.common.collect.ImmutableList;
import services.program.ProgramDefinition;

class Updater {
  private ApplicantData applicantData;

  Updater(ApplicantData applicantData) {
    this.applicantData = applicantData;
  }

  /** Performs updates on applicant data. */
  void update(ImmutableList<Update> updates) {}

  // TODO(natsid): This should return List<ValidationError> once ValidationError exists.
  /** Runs validation logic on applicant data. */
  ImmutableList<String> validate(ProgramDefinition programDefinition) {
    return null;
  }

  /** Saves applicantData to the database. */
  void save() {
    // Need to verify validate has been called and/or do some other data scrubbing before saving.
  }
}