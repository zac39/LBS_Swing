import java.awt.*;

public abstract class AWTHelper {

    public static Component findChildComponentByName(Container parentComponent, String childName) {
        // Verifica se il componente parentComponent ha il nome desiderato
        if (childName.equals(parentComponent.getName())) {
            return parentComponent;
        }

        // Recupera i componenti figlio del parentComponent
        Component[] components = parentComponent.getComponents();

        // Cerca ricorsivamente nei componenti figlio
        for (Component component : components) {
            if (component instanceof Container) {
                // Se il componente figlio è un contenitore, esegui la ricerca all'interno di esso
                Component foundComponent = findChildComponentByName((Container) component, childName);
                if (foundComponent != null) {
                    return foundComponent;
                }
            } else if (childName.equals(component.getName())) {
                // Se il componente figlio ha il nome desiderato, restituiscilo
                return component;
            }
        }

        // Se non viene trovato nessun componente con il nome desiderato, restituisci null
        return null;
    }
}
