package org.students.simplebitcoinwallet.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ValidationErrorResponse {
    private String errorMessage;
    private Integer httpCode;
    private Map<String, String> invalidObjects;
}
