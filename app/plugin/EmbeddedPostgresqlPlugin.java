package plugin;

import java.io.IOException;

import de.flapdoodle.embed.process.distribution.GenericVersion;
import de.flapdoodle.embed.process.distribution.IVersion;
import play.Logger;
import play.Play;
import play.PlayPlugin;
import ru.yandex.qatools.embed.postgresql.EmbeddedPostgres;

public class EmbeddedPostgresqlPlugin extends PlayPlugin {

    private final IVersion PG_VERSION = new GenericVersion("9.3.20-1");
    private final EmbeddedPostgres postgres = new EmbeddedPostgres(this.PG_VERSION);
    private String url;

    @Override
    public void onLoad() {
        Logger.info("onLoad: %s", getClass());
        if (Play.runingInTestMode()) {
            try {
                this.url = this.postgres.start("localhost", 9876, "test", "test", "test");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            Logger.info("EmbeddedPostgresqlPlugin is disabled");
            Play.pluginCollection.disablePlugin(this);
        }
    }

    @Override
    public void onConfigurationRead() {
        Logger.info("onConfigurationRead: %s", getClass());
        if (Play.runingInTestMode()) {
            Play.configuration.setProperty("db", "");
            Play.configuration.setProperty("db.url", this.url);
        }
    }

}
