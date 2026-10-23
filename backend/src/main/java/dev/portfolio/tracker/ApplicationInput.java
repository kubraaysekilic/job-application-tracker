package dev.portfolio.tracker;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
public record ApplicationInput(
 @NotBlank @Size(max=120) String company,
 @NotBlank @Size(max=160) String position,
 @Size(max=1000) String url,
 @NotNull @PastOrPresent LocalDate appliedOn,
 @NotNull Status status,
 @Size(max=3000) String notes,
 @PositiveOrZero Long version
) {}
