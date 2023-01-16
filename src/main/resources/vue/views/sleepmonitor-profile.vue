<template id="sleepmonitor-profile">
  <app-layout>
    <div v-if="noDataFound">
      <p> We're sorry, we were not able to retrieve this information.</p>
      <p> View <a :href="'/sleepmonitor'">all information</a>.</p>
    </div>
    <div class="card bg-light mb-3" v-if="!noDataFound">
      <div class="card-header">
        <div class="row">
          <div class="col-6"> Sleep Information Profile </div>
          <div class="col" align="right">
            <button rel="tooltip" title="Update"
                    class="btn btn-info btn-simple btn-link"
                    @click="updateSleepInfo()">
              <i class="far fa-save" aria-hidden="true"></i>
            </button>
            <button rel="tooltip" title="Delete"
                    class="btn btn-info btn-simple btn-link"
                    @click="deleteSleepInfo()">
              <i class="fas fa-trash" aria-hidden="true"></i>
            </button>
          </div>
        </div>
      </div>
      <div class="card-body">
        <form id="addSleepInfo">
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-name">User Id</span>
            </div>
            <input type="" class="form-control" v-model="sleepmonitor.user_id" name="User Id" placeholder="User Id"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-date">Date</span>
            </div>
            <input type="date" class="form-control" v-model="sleepmonitor.date" name="Date" placeholder="Date"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-day">Day</span>
            </div>
            <input type="text" class="form-control" v-model="sleepmonitor.day" name="Day" placeholder="Day"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-sleep-duration">Sleep Duration</span>
            </div>
            <input type="" class="form-control" v-model="sleepmonitor.sleepDuration" name="Sleep Duration" placeholder="Sleep Duration"/>
          </div>
        </form>
      </div>
      <div class="card-footer text-left">
      </div>
    </div>
  </app-layout>
</template>

<script>
Vue.component("sleepmonitor-profile", {
  template: "#sleepmonitor-profile",
  data: () => ({
    sleepmonitor: null,
    noDataFound: false,
  }),
  created: function () {
    const sleepMonitorId = this.$javalin.pathParams["sleepmonitor-id"];
    const url = `/api/sleepmonitoring/${sleepMonitorId}`
    axios.get(url)
        .then(res => this.sleepmonitor = res.data)
        .catch(error => {
          console.log("No data found in the path parameter: " + error)
          this.noDataFound = true
        })
  },
  methods: {
    updateSleepInfo: function () {
      const sleepInfoId = this.$javalin.pathParams["sleepmonitor-id"];
      const url = `/api/sleepmonitoring/${sleepInfoId}`
      axios.patch(url,
          {
            user_id: this.sleepmonitor.user_id,
            date: this.sleepmonitor.date,
            day: this.sleepmonitor.day,
            sleepDuration: this.sleepmonitor.sleepDuration
          })
          .then(response =>
              this.user.push(response.data))
          .catch(error => {
            console.log(error)
          })
      alert("Data updated!")
    },
    deleteSleepInfo: function () {
      if (confirm("Do you really want to delete?")) {
        const sleepInfoId = this.$javalin.pathParams["sleepmonitor-id"];
        const url = `/api/sleepmonitoring/${sleepInfoId}`
        axios.delete(url)
            .then(response => {
              alert("Data deleted")
              //display the /users endpoint
              window.location.href = '/sleepmonitor';
            })
            .catch(function (error) {
              console.log(error)
            });
      }
    }
  }
});
</script>