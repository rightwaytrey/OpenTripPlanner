package org.opentripplanner.apis.transmodel.mapping;

import java.util.Map;
import java.util.function.Consumer;
import org.opentripplanner.routing.api.request.RequestModes;
import org.opentripplanner.routing.api.request.RequestModesBuilder;
import org.opentripplanner.routing.api.request.StreetMode;

class RequestStreetModesMapper {

  private static final String ACCESS_MODE_KEY = "accessMode";
  private static final String EGRESS_MODE_KEY = "egressMode";
  private static final String DIRECT_MODE_KEY = "directMode";

  /**
   * Maps GraphQL Modes input type to RequestModes.
   * <p>
   * This only maps access, egress, direct & transfer modes. Transport modes are set using filters.
   */
  static RequestModes mapRequestStreetModes(Map<String, ?> modesInput) {
    RequestModesBuilder mBuilder = RequestModes.of();

    final StreetMode accessMode = (StreetMode) modesInput.get(ACCESS_MODE_KEY);
    ensureValueAndSet(accessMode, mBuilder::withAccessMode);
    ensureValueAndSet((StreetMode) modesInput.get(EGRESS_MODE_KEY), mBuilder::withEgressMode);
    ensureValueAndSet((StreetMode) modesInput.get(DIRECT_MODE_KEY), mBuilder::withDirectMode);
    // Note: Transfer mode is NOT auto-set from access mode to avoid unintended filtering.
    // Auto-setting transfer mode to BIKE triggers the requireBikesAllowed filter, which
    // filters out all trips without explicit bikes_allowed=ALLOWED in GTFS data.
    // Most feeds have bikes_allowed=UNKNOWN, causing all trips to be filtered.
    // Users who want to bike between transit stops (and need bikes on board) should
    // explicitly set the transfer mode in their request.

    return mBuilder.build();
  }

  /**
   * Use the provided consumer to apply the StreetMode if it's non-null, otherwise apply NOT_SET.
   *
   * @param streetMode
   * @param consumer
   */
  private static void ensureValueAndSet(StreetMode streetMode, Consumer<StreetMode> consumer) {
    consumer.accept(streetMode == null ? StreetMode.NOT_SET : streetMode);
  }
}
