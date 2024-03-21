import java.util.List;

public class IrisExample {
    private final List<Double> attributes;
    private final String decisionAttribute;

    public IrisExample(List<Double> attributes, String decisionAttribute) {
        this.attributes = attributes;
        this.decisionAttribute = decisionAttribute;
    }

    public List<Double> getAttributes() {
        return attributes;
    }

    public String getDecisionAttribute() {
        return decisionAttribute;
    }
}
