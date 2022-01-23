package table.sources;

import org.apache.flink.connector.datagen.table.DataGenConnectorOptions;
import org.apache.flink.streaming.api.functions.source.datagen.DataGenerator;
import org.apache.flink.table.api.DataTypes;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Schema;
import org.apache.flink.table.api.TableDescriptor;
import org.apache.flink.table.api.TableEnvironment;

public class fileSources {

	public static void main(String[] args) {
		
		EnvironmentSettings settings = EnvironmentSettings.newInstance().inBatchMode().build();
		TableEnvironment tableEnv = TableEnvironment.create(settings);
		
		tableEnv.createTemporaryTable("csvSourceTable", TableDescriptor
				.forConnector("datagen").schema(Schema.newBuilder()
		        .column("f0", DataTypes.STRING())
		        .column("f1", DataTypes.STRING())
		        .build())
				.option(DataGenConnectorOptions.ROWS_PER_SECOND, 100L)
			    .build());
				
		
		
	}
}
