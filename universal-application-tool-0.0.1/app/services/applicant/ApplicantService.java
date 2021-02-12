package services.applicant;

import com.google.common.collect.ImmutableList;
import services.program.ProgramDefinition;

import java.util.concurrent.CompletionStage;

/** Operations that can be performed on an {@link ApplicantData}. */
public interface ApplicantService {

  // TODO(natsid): This should return List<ValidationError> once ValidationError exists.
  /**
   * Updates applicantData with updates if possible. If the updates aren't valid in the context of
   * programDefinition, doesn't save the updates to the database and instead returns a list of
   * ValidationErrors.
   *
   * @param applicantId the ID  of the applicant to update
   * @param programId the ID of the program in which the updates are being made
   * @param updates the updates to attempt
   * @return
   */
  CompletionStage<ImmutableList<String>> update(
      long applicantId,
      long programId,
      ImmutableList<Update> updates);

  // getCurrentState
}
