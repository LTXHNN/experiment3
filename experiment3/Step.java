package experiment3;

/**
 * @author 李天翔
 * @date 2022/05/30
 **/
public class Step {
    int numStep;
    String stateStack;
    String characterStack;
    String inString;
    String productString;

    public Step(int numStep, String stateStack, String characterStack, String inString, String productString) {
        this.numStep = numStep;
        this.stateStack = stateStack;
        this.characterStack = characterStack;
        this.inString = inString;
        this.productString = productString;
    }

    @Override
    public String toString() {
        return
                numStep +
                        "\t\t\t" + stateStack + "\t\t\t"+
                        characterStack + "\t\t\t" +
                        inString + "\t\t\t" +
                        productString;
    }
}
