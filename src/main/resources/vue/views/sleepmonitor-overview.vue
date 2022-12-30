<template id="sleepmonitor-overview">
  <div class="container">
    <h3 class="p-3 text-center">Sleep Monitor Information</h3>
    <table class="table table-striped table-bordered">
      <thead>
      <tr>
        <th>Date</th>
        <th>Day</th>
        <th>Sleep Duration</th>
        <th>User ID</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="sleepmonitorInfo in sleepmonitor" :key="sleepmonitorInfo.id">
        <td>{{sleepmonitorInfo.date}}</td>
        <td>{{sleepmonitorInfo.day}}</td>
        <td>{{sleepmonitorInfo.sleepDuration}}</td>
        <td>{{sleepmonitorInfo.user_id}}</td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
Vue.component("sleepmonitor-overview",{
  template: "#sleepmonitor-overview",
  data: () => ({
    sleepmonitor: []
  }),
  created() {
    this.fetchSleepMonitorInfo();
    axios.get(`/api/sleepmonitoring`)
        .then(res => this.sleepmonitor = res.data)
        .catch(() => alert("Error while fetching sleepmonitoring info"));
  },
  methods: {
    fetchSleepMonitorInfo: function () {
      axios.get("/api/sleepmonitoring")
          .then(res => this.sleepmonitor = res.data)
          .catch(() => alert("Error while fetching sleepmonitoring info"));
    }
  }
});
</script>