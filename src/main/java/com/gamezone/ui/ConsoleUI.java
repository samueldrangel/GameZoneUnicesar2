
import com.gamezone.ui.AccessoryMenu;
import com.gamezone.ui.ConsoleInput;
import com.gamezone.ui.PersonMenu;
import com.gamezone.ui.ProductMenu;
import com.gamezone.ui.PromotionMenu;
import com.gamezone.ui.ReturnMenu;
import com.gamezone.ui.SaleMenu;
import com.gamezone.ui.WarrantyMenu;

public class ConsoleUI {
    private final ConsoleInput input;
    private final ProductMenu productMenu;
    private final AccessoryMenu accessoryMenu;
    private final PersonMenu personMenu;
    private final SaleMenu saleMenu;
    private final PromotionMenu promotionMenu;
    private final WarrantyMenu warrantyMenu;
    private final ReturnMenu returnMenu;
    // constructor assigns the eight collaborators

    public ConsoleUI() {
        this.input = null;
        this.productMenu = null;
        this.accessoryMenu = null;
        this.personMenu = null;
        this.saleMenu = null;
        this.promotionMenu = null;
        this.warrantyMenu = null;
        this.returnMenu = null;
    }

    /** Shows the main menu until the user exits. */
    public void showMainMenu() {
        int option;
        do {
            // same println lines as today
            option = input.readInt("Seleccione una opción: ");
            switch (option) {
                case 1: productMenu.show(); break;
                case 2: accessoryMenu.show(); break;
                case 3: personMenu.show(); break;
                case 4: saleMenu.show(); break;
                case 5: promotionMenu.show(); break;
                case 6: warrantyMenu.show(); break;
                case 7: returnMenu.show(); break;
                case 0: System.out.println("¡Gracias por usar GameZone Unicesar!"); break;
                default: System.out.println("Opción inválida.");
            }
        } while (option != 0);
    }
}
