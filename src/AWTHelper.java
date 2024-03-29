import java.awt.*;

abstract class AWTHelper {
    public static Component findChildComponentByName(Container parent, String name) {
        Component[] components = parent.getComponents();
        for (Component component : components) {
            if (component instanceof Container) {
                Component child = findChildComponentByName((Container) component, name);
                if (child != null) {
                    return child;
                }
            }
            if (name.equals(component.getName())) {
                return component;
            }
        }
        return null;
    }
}
