package table.api;

import org.apache.flink.connector.datagen.table.DataGenConnectorOptions;
import org.apache.flink.table.api.DataTypes;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Schema;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableDescriptor;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.TableResult;

/**
 * 使用简单用法
 * @author Lenovo
 *
 */
public class TestApi2 {

	public static void main(String[] args) throws Exception {
		EnvironmentSettings settings = EnvironmentSettings.newInstance().inStreamingMode().build();
		TableEnvironment tableEnv = TableEnvironment.create(settings);

		tableEnv.createTemporaryTable("SourceTable", TableDescriptor.forConnector("datagen")
				.schema(Schema.newBuilder()
						.column("f0", DataTypes.STRING())
						.column("f1", DataTypes.INT())
						.column("f2", DataTypes.INT())
						.build())
			    .option(DataGenConnectorOptions.ROWS_PER_SECOND, 100L)
				.build());
		
		tableEnv.executeSql("CREATE TABLE SinkTable with ('connector' = 'blackhole') like SourceTable (EXCLUDING ALL)");
		
		Table table2 = tableEnv.from("SourceTable");
		
		Table table3 = tableEnv.sqlQuery("select * from SourceTable");
		
		TableResult result = table2.executeInsert("SinkTable");
		
		table3.execute().print();
		
		result.print();
		
	}
	
	
}
