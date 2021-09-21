package interactions;

public interface IButtonEventHandler {
    boolean verifyComponentId(String componentId);
    String getCommandName(String componentId);
}
