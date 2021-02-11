package services.applicant;

import com.google.common.collect.ImmutableList;
import services.program.ProgramDefinition;

/** Operations that can be performed on an {@link ApplicantData}. */
public interface ApplicantService {

  // TODO(natsid): This should return List<ValidationError> once ValidationError exists.
  /**
   * Updates applicantData with updates if possible. If the updates aren't valid in the context of
   * programDefinition, doesn't save the updates to the database and instead returns a list of
   * ValidationErrors.
   *
   * @param applicantData the applicant data to update
   * @param updates the updates to attempt
   * @param programDefinition the program context in which the updates are being made
   */
  ImmutableList<String> update(
      ApplicantData applicantData,
      ImmutableList<Update> updates,
      ProgramDefinition programDefinition);
}
