package interactions;

public class ButtonEventHandler implements IButtonEventHandler {
    @Override
    public boolean verifyComponentId(String componentId) {
        return componentId.split(":").length > 0;
    }

    @Override
    public String getCommandName(String componentId) {
        return componentId.split(":")[0];
    }
}
