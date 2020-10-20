import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class Restoration {
	public Restoration(Crud crud) throws SQLException, FileNotFoundException {
		for(String tableName: crud.getTableNames()) {
			crud.setWorkingTable(tableName);
			crud.deleteAllRecords();
		}
		Scanner scanner = new Scanner(new File("inventory_team4.csv"));
		String[] headers = scanner.nextLine().split(",");
		headers[0] = crud.removeUTF8BOM(headers[0]);
		ArrayList<Object[]> list = new ArrayList<>();
		while(scanner.hasNextLine()) {
			String[] line  = scanner.nextLine().split(",");
			//IKQHDHWV0FN3,1445,134.22,183.88,DYCUYQFX
			String productId = line[0];
			int quantity = Integer.parseInt(line[1]);
			double wholesaleCost = Double.parseDouble(line[2]);
			double salePrice = Double.parseDouble(line[3]);
			String supplierId = line[4];
			list.add(new Object[] {productId, quantity, wholesaleCost, salePrice, supplierId});
		}
		Object[][] inventory = new Object[list.size()][headers.length];
		Iterator<Object[]> it = list.iterator();
		int i = 0; 
		while(it.hasNext()) {
			inventory[i] = it.next();
			i++;
		}
		crud.setWorkingTable("inventory");
		crud.insertRecords(headers, inventory);
		
		new SalesProcessor(crud).processItems("customer_orders_A_team4.csv");
	}
}
