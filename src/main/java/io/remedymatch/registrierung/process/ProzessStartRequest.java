package io.remedymatch.registrierung.process;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class ProzessStartRequest {
    private String prozessKey;
    private String businessKey;
    private Map<String, Object> variables;
}
