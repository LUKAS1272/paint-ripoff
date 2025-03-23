package adapters;

import enums.Alignment;
import enums.LineType;
import enums.storeEnums.Enum;
import stores.EnumStore;
import utilities.Frame;
import utilities.Renderer;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Keyboard {
    // Singleton functions
    private static Keyboard instance;

    public static Keyboard getInstance() {
        if (instance == null) {
            instance = new Keyboard();
        }
        return instance;
    }
    // Singleton functions end


    // Initializes keyAdapter and creates a listener on panel
    private KeyAdapter keyAdapter;
    private Keyboard() {
        CreateKeyboardAdapter(); // Initialize
        Frame.getInstance().getPanel().addKeyListener(keyAdapter);
    }

    private final ArrayList<Integer> pressedKeys = new ArrayList<>();

    private void CreateKeyboardAdapter() {
        keyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    Renderer.getInstance().clear();
                }

                pressedKeys.add(e.getKeyCode());
                update();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                pressedKeys.remove(Integer.valueOf(e.getKeyCode()));
                update();
            }

            private void update() {
                updateMode();
                updateAlignment();
                updateObject();
            }

            private void updateMode() {
                if (pressedKeys.isEmpty()) {
                    EnumStore.getInstance().lineType = LineType.Default;
                    return;
                }

                switch (pressedKeys.getFirst()) {
                    case KeyEvent.VK_CONTROL:
                        EnumStore.getInstance().lineType = LineType.Dotted;
                        break;
                    case KeyEvent.VK_ALT:
                        EnumStore.getInstance().lineType = LineType.Dashed;
                        break;
                    default:
                        EnumStore.getInstance().lineType = LineType.Default;
                }
            }

            private void updateAlignment() {
                if (pressedKeys.contains(KeyEvent.VK_SHIFT)) {
                    EnumStore.getInstance().alignment = Alignment.Aligned;
                } else {
                    EnumStore.getInstance().alignment = Alignment.Unaligned;
                }
            }

            private void updateObject() {
                if (pressedKeys.contains(KeyEvent.VK_E)) {
                    EnumStore.getInstance().moveEnum(Enum.ActionType);
                    Renderer.getInstance().rerender();
                }
            }
        };
    }
}
