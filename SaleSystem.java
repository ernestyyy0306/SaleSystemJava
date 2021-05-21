import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class SaleSystem {


    static class Items implements Comparable<Items>{
        private String name;
        private double price;
        private int quantity;
        private int calories;
        private double regularPrice;
        private int regularCalories;
        private int upsizeCalories;
        private double upsizePrice;
        private String category;
        private String size;
        private String imgUrl;

        public Items(String _name, double _price, int _calories, String _size, 
            String _category, double _upsizePrice, int _upsizeCalories, String _imgUrl){
                name = _name;
                price = _price;
                size = _size;
                regularPrice = _price;
                regularCalories = _calories;
                calories = _calories;
                quantity = 0;
                category = _category;
                upsizeCalories = _upsizeCalories;
                upsizePrice = _upsizePrice;
                imgUrl = _imgUrl;
        }

        public Items(String _name, double _price, int _calories, String _size, String _category){
                name = _name;
                price = _price;
                calories = _calories;
                size = _size;
                category = _category;
        }

        public String getName(){return name;}
        public double getPrice(){return price;}
        public int getCalories(){return calories;}
        public int getQuantity(){return quantity;}
        public String getSize(){return size;}
        public String getCategory(){return category;}
        public double getUpsizePrice(){return upsizePrice;}
        public int getUpsizeCalories(){return upsizeCalories;}
        public double getRegularPrice(){return regularPrice;}
        public int getRegularCalories(){return regularCalories;}

        public void setQuantity(int _quantity){quantity = _quantity;}
        public void setName(String _name){name = _name;}
        public void setPrice(double _price){price = _price;}
        public void setSize(String _size){size = _size;}
        
        public void updateItems(){
            if(size.equals("Upsized")){
                size = "Upsized";
                price = upsizePrice;
                calories = upsizeCalories;
            }
            else{
                size = "Regular";
                price = regularPrice;
                calories = regularCalories;
            }
        }

        public String toString(){
            return "Item: " + name + ", price: RM" + String.format("%.2f", (price)) + ", Calories: " 
                + calories + "cal, Size: " + size + "\n";
        }
        
        public int compareTo(Items item){
            return (this.getPrice() < item.getPrice() ? 1 : 
                    (this.getPrice() == item.getPrice() ? 0 : -1));     
        }

        public String getImgUrl() {
            return imgUrl;
        }
    }

    static class Interface extends JPanel{
        private static final long serialVersionUID = 1L;
        private ArrayList<Items> items = new ArrayList<Items>();
        private ArrayList<Items> cart = new ArrayList<Items>();
        private String order;
        private String invoice;
        private JTextArea textAreaOrder = new JTextArea(40, 40);
        private JTextArea textAreaInvoice = new JTextArea(40, 40);
        private JTextArea manualTextArea = new JTextArea(80,50);
        private JButton reset = new JButton("reset");
        private JButton addCart = new JButton("add");
        private JButton checkout = new JButton("checkout");
        private Orders orders = new Orders();

        public Interface(ArrayList<Items> _items){
            JPanel panel1 = new JPanel();
            JPanel panel2 = new JPanel();
            JScrollPane invoiceScroll = new JScrollPane(textAreaInvoice);
            JScrollPane orderScroll = new JScrollPane(textAreaOrder);
            JScrollPane manualScroll = new JScrollPane(manualTextArea);
            String manual = "Welcome to Cincailahh's Fastfood Meal Sale System.\n" +
                            "New promotion!\n" +
                            "Choose a side and drink item each to be bundle with a main item to get 20% discount!\n" +
                            "The most expensive items will be bundled up.\n" +
                            "\n====================================================\n" +
                            "Manual:\nreset: reset everything.\nadd: add items to order list.\n" +
                            "checkout: calculate final price and calories in invoice." +
                            "\n====================================================\n";
            manualTextArea.setText(manual);
            invoiceScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            orderScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            manualScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            panel1.setLayout(new GridLayout(0, 2));
            panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
            items = _items;
            order = "Orders: \n";
            invoice = "Invoice: \n";
            textAreaOrder.setText("Orders: ");
            textAreaInvoice.setText("Invoice: ");
            panel1.add(orderScroll);
            panel1.add(invoiceScroll);
            panel2.add(upperInterface());
            panel2.add(panel1);
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            add(panel2);
            add(manualScroll);
        }

        public JPanel upperInterface(){
            JLabel mains = new JLabel("Mains:");
            JLabel sides = new JLabel("Sides:");
            JLabel drinks = new JLabel("Drinks:");
            JPanel main = new JPanel();
            main.setLayout(new GridLayout(0, 2));
            main.add(controls(0));
            main.add(controls(1));
            JPanel side = new JPanel();
            side.setLayout(new GridLayout(0, 2));
            side.add(controls(2));
            side.add(controls(3));
            JPanel drink = new JPanel();
            drink.setLayout(new GridLayout(0, 2));
            drink.add(controls(4));
            drink.add(controls(5));
            JPanel buttons = new JPanel();
            buttons.setLayout(new GridLayout(0, 3));
            buttons.add(reset);
            buttons.add(addCart);
            buttons.add(checkout);
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.add(mains);
            panel.add(main);
            panel.add(sides);
            panel.add(side);
            panel.add(drinks);
            panel.add(drink);
            panel.add(buttons);
            checkout.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    invoice = "Invoice: \n";
                        ArrayList<Items> temps = new ArrayList<Items>();
                    for(int i = 0; i < cart.size(); i++){
                        Items item = new Items(cart.get(i).getName(), cart.get(i).getPrice(), 
                        cart.get(i).getCalories(), cart.get(i).getSize(), cart.get(i).getCategory());
                        temps.add(item);
                    }
                    orders.clear();
                    orders.addOrders(temps);
                    invoice = invoice + orders.getOrders();
                    textAreaInvoice.setText(invoice);
            }});
            return panel;
        }

        public JPanel controls(int i){
            JPanel imgPanel = new JPanel();
            JLabel imgContainer = new JLabel(new ImageIcon(items.get(i).getImgUrl()));
            imgContainer.setHorizontalAlignment(JLabel.CENTER);
            imgPanel.add(imgContainer);

            JPanel panel3 = new JPanel(); 
            JLabel name = new JLabel(items.get(i).getName());
            JLabel price = new JLabel("RM" + Double.toString(items.get(i).getPrice()));
            JLabel calories = new JLabel(Integer.toString(items.get(i).getCalories()) + "cal");
            name.setHorizontalAlignment(JLabel.CENTER);
            price.setHorizontalAlignment(JLabel.CENTER);
            calories.setHorizontalAlignment(JLabel.CENTER);
            panel3.setLayout(new GridLayout(3, 0));
            panel3.add(name);
            panel3.add(price);
            panel3.add(calories);
            JPanel panel1 = new JPanel();  
            JTextArea textAreaSize = new JTextArea(1, 5);
            textAreaSize.setText("Regular");
            JButton size = new JButton("Size");;
            panel1.add(textAreaSize);
            panel1.add(size);
            size.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    if(items.get(i).getSize().equals("Upsized")){
                        textAreaSize.setText("Regular");
                        items.get(i).setSize("Regular");
                        price.setText("RM" + Double.toString(items.get(i).getRegularPrice()));
                        calories.setText(Integer.toString(items.get(i).getRegularCalories()) + "cal");
                    }
                    else{
                        textAreaSize.setText("Upsized");
                        items.get(i).setSize("Upsized");
                        price.setText("RM" + Double.toString(items.get(i).getUpsizePrice()));
                        calories.setText(Integer.toString(items.get(i).getUpsizeCalories()) + "cal");
                    }
            }});
            JTextArea textAreaQuantity = new JTextArea(1, 3);
            textAreaQuantity.setText("0");
            JPanel panel2 = new JPanel();  
            JButton minus = new JButton("-");;
            JButton add = new JButton("+");;
            panel2.add(minus);
            panel2.add(textAreaQuantity);
            panel2.add(add);
            minus.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    if(items.get(i).quantity > 0){
                        items.get(i).setQuantity(items.get(i).getQuantity() - 1);
                        textAreaQuantity.setText(Integer.toString(items.get(i).getQuantity()));
                    }
                    textAreaQuantity.setText(Integer.toString(items.get(i).getQuantity()));
            }});
            add.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    items.get(i).setQuantity(items.get(i).getQuantity() + 1);
                    textAreaQuantity.setText(Integer.toString(items.get(i).getQuantity()));
            }});
            reset.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                items.get(i).setSize("Regular");
                textAreaSize.setText("Regular");
                items.get(i).setQuantity(0);
                textAreaQuantity.setText(Integer.toString(items.get(i).getQuantity()));
                textAreaOrder.setText("Orders: ");
                textAreaInvoice.setText("Invoice: ");
                order = "Orders: \n";
                invoice = "Invoice: \n";
                cart.clear();
                orders.clear();
            }});
            addCart.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    int count = 0;
                    while(items.get(i).getQuantity() > count){
                        items.get(i).updateItems();
                        Items item = new Items(items.get(i).getName(), items.get(i).getPrice(), 
                            items.get(i).getCalories(), items.get(i).getSize(), items.get(i).getCategory());
                        cart.add(item);
                        order = order + items.get(i).toString();
                        count = count + 1;
                    }
                    textAreaOrder.setText(order);
                    items.get(i).setQuantity(0);
                    textAreaQuantity.setText(Integer.toString(items.get(i).quantity));
            }});
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            JPanel imgPanel_and_panel3 = new JPanel();
            imgPanel_and_panel3.setLayout(new FlowLayout());
            imgPanel_and_panel3.add(imgPanel);
            imgPanel_and_panel3.add(panel3);
            //panel.add(imgPanel);
            //panel.add(panel3);
            panel.add(imgPanel_and_panel3);
            JPanel panel1_and_panel2 = new JPanel();
            panel1_and_panel2.setLayout(new FlowLayout());
            panel1_and_panel2.add(panel2);
            panel1_and_panel2.add(panel1);
            panel.add(panel1_and_panel2);
            return panel;
        }
    }

    static class Orders{
        private ArrayList<Items> orders;
        private ArrayList<Items> mains;
        private ArrayList<Items> sides;
        private ArrayList<Items> drinks;
        private ArrayList<Items> bundles;
        private double bundlePrice;
        private double singlePrice;
        private double totalPrice;
        private int bundleCalory;
        private int singleCalory;
        private int totalCalory;

        public Orders(){
            orders = new ArrayList<Items>();
            mains = new ArrayList<Items>();
            sides = new ArrayList<Items>();
            drinks = new ArrayList<Items>();
            bundles = new ArrayList<Items>();
            bundlePrice = 0;
            singlePrice = 0;
            totalPrice = 0;
            bundleCalory = 0;
            singleCalory = 0;
            totalCalory = 0;
        }

        public void addOrders(ArrayList<Items> _orders){
            orders = _orders;
            sortCategory();
        }

        public void sortCategory(){
            Collections.sort(orders);
            for(int i = 0; i < orders.size(); i++){
                if(orders.get(i).getCategory().equals("Mains")){mains.add(orders.get(i));}
                if(orders.get(i).getCategory().equals("Sides")){sides.add(orders.get(i));}
                if(orders.get(i).getCategory().equals("Drinks")){drinks.add(orders.get(i));}
            }
            bundle();
        }

        public void bundle(){
            int mainCount = mains.size();
            for(int i = 0; i < mainCount; i++){
                if(sides.size() != 0 && drinks.size() != 0){
                    bundles.add(mains.get(0));
                    bundles.add(sides.get(0));
                    bundles.add(drinks.get(0));
                    mains.remove(0);
                    sides.remove(0);
                    drinks.remove(0);
                }
            }
        }

        public void clear(){
            bundles.clear();
            orders.clear();
            mains.clear();
            sides.clear();
            drinks.clear();
            bundlePrice = 0;
            singlePrice = 0;
            totalPrice = 0;
            bundleCalory = 0;
            singleCalory = 0;
            totalCalory = 0;
        }

        public void calculate(){
            bundlePrice = 0;
            singlePrice = 0;
            totalPrice = 0;
            singleCalory = 0;
            bundleCalory = 0;
            totalCalory = 0;
            for(int i = 0; i < bundles.size(); i++){
                bundlePrice += bundles.get(i).getPrice();
                bundleCalory += bundles.get(i).getCalories();
            }
            for(int i = 0; i < mains.size(); i++){
                singlePrice += mains.get(i).getPrice();
                singleCalory += mains.get(i).getCalories();
            }
            for(int i = 0; i < sides.size(); i++){
                singlePrice += sides.get(i).getPrice();
                singleCalory += sides.get(i).getCalories();
            }
            for(int i = 0; i < drinks.size(); i++){
                singlePrice += drinks.get(i).getPrice();
                singleCalory += drinks.get(i).getCalories();
            }
            totalPrice = singlePrice + (bundlePrice * 0.8);
            totalCalory = singleCalory + bundleCalory;
        }

        public String getOrders(){
            String invoice = "";
            if(bundles.size() != 0){   
                invoice = invoice + "Bundle Promo Detected (20% Discount): \n";
                for(int i = 0; i < bundles.size(); i++){
                    invoice = invoice + bundles.get(i).toString();
                }
                calculate();
                invoice = invoice + "\nNormal Price: RM" + String.format("%.2f", (bundlePrice)) + "\n";
                invoice = invoice + "Bundle Price: RM" + String.format("%.2f", (bundlePrice * 0.8)) + "\n";
                invoice = invoice + "Calories: " + bundleCalory + "cal\n";
                
            }
            if(!(mains.size() == 0 && sides.size() == 0 && drinks.size() == 0)){
                invoice = invoice + "===========================================================================\n";
                invoice = invoice + "\nSingle items: \n";
                for(int i = 0; i < mains.size(); i++){
                    invoice = invoice + mains.get(i).toString();
                }
                for(int i = 0; i < sides.size(); i++){
                    invoice = invoice + sides.get(i).toString();
                }
                for(int i = 0; i < drinks.size(); i++){
                    invoice = invoice + drinks.get(i).toString();
                }
                calculate();
                invoice = invoice + "\nSingle Price: RM" + String.format("%.2f", (singlePrice)) + "\n";
                invoice = invoice + "Calories: " + singleCalory + "cal\n";
            }

            invoice = invoice + "\n===========================================================================\n";
            invoice = invoice + "\nTotal Price: RM" + String.format("%.2f", (totalPrice)) + "\n";
            invoice = invoice + "Total Calories: " + totalCalory + "cal\n";
            return invoice;
        }
    }

    public static void main(final String[] args){
        Items burger = new Items("Burger", 5.00, 295, "Regular", "Mains", 6.50, 412, "burgerJPG.jpg");
        Items friedChicken = new Items("Fried Chicken", 5.50, 246, "Regular", "Mains", 7.00, 375,"friedChickenJPG.jpg");
        Items fries = new Items("Fries", 3.50, 312, "Regular", "Sides", 5.00, 485,"friesJPG.jpg");
        Items coleslaw = new Items("Coleslaw", 3.00, 152, "Regular", "Sides", 4.00, 214,"coleslawJPG.jpg");
        Items hotBeverage = new Items("Hot Beverage", 1.50, 129, "Regular", "Drinks", 2.50, 185,"rsz_hotbeverage.jpg");
        Items softDrinks = new Items("Soft Drinks", 2.00, 150, "Regular", "Drinks", 3.00, 250,"softdrinkJPG.jpg");
        
        ArrayList<Items> items = new ArrayList<Items>();
        items.add(burger);
        items.add(friedChicken);
        items.add(fries);
        items.add(coleslaw);
        items.add(hotBeverage);
        items.add(softDrinks);

        JFrame frame = new JFrame("Fastfood Meal Sale System");
        frame.add(new Interface(items));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
		frame.setVisible(true);
    }
}
