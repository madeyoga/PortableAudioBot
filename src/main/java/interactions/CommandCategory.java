package interactions;

import org.jetbrains.annotations.NotNull;

public class CommandCategory implements ICommandCategory {
    private final String name;

    public CommandCategory(@NotNull String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
