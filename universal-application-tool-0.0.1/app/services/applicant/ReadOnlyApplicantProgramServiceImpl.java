package services.applicant;

import com.google.common.collect.ImmutableList;
import models.Applicant;
import services.program.ProgramDefinition;
import java.util.Optional;

/** Provides synchronous, read-only behavior relevant to an applicant for a specific program. */
public class ReadOnlyApplicantProgramServiceImpl implements ReadOnlyApplicantProgramService {
  /** Get the program's current Blocks for the applicant. */
  ImmutableList<Block> getCurrentBlockList() {
    return null;
  }

  /** Get the block that comes after the block with the given ID if there is one. */
  Optional<Block> getBlockAfter(long blockId) {
    return null;
  }

  /** Get the block that comes after the given block if there is one. */
  Optional<Block> getBlockAfter(Block block) {
    return null;
  }

  /** Get the program block with the lowest index that has missing answer data if there is one. */
  Optional<Block> getFirstIncompleteBlock() {
    return null;
  }
}
