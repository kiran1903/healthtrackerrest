<template id="healthparameter-profile">
  <app-layout>
    <div v-if="noParameterFound">
      <p> We're sorry, we were not able to retrieve this data.</p>
      <p> View <a :href="'/healthparameters'">all Health Parameters Info</a>.</p>
    </div>
    <div class="card bg-light mb-3" v-if="!noParameterFound">
      <div class="card-header">
        <div class="row">
          <div class="col-6"> Health Parameter Profile </div>
          <div class="col" align="right">
            <button rel="tooltip" title="Update"
                    class="btn btn-info btn-simple btn-link"
                    @click="updateHealthParameter()">
              <i class="far fa-save" aria-hidden="true"></i>
            </button>
            <button rel="tooltip" title="Delete"
                    class="btn btn-info btn-simple btn-link"
                    @click="deleteHealthParameter()">
              <i class="fas fa-trash" aria-hidden="true"></i>
            </button>
          </div>
        </div>
      </div>
      <div class="card-body">
        <form>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-healthparameter-id">Health parameter ID</span>
            </div>
            <input type="number" class="form-control" v-model="healthparameter.id" name="id" readonly placeholder="Id"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-healthparameter-name">Blood Pressure</span>
            </div>
            <input type="text" class="form-control" v-model="healthparameter.bloodpressure" name="bloodpressure" placeholder="Blood Pressure"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-healthparameter-email">Glucose</span>
            </div>
            <input type="email" class="form-control" v-model="healthparameter.glucose" name="glucose" placeholder="Glucose"/>
          </div>
        </form>
      </div>
      <div class="card-footer text-left">
        <p  v-if="healthparameters.length == 0"> No Parameters data yet...</p>
        <p  v-if="healthparameters.length > 0"> Health Parameters info so far...</p>
        <ul>
          <li v-for="healthparameter in healthparameters">
            {{ healthparameter.bloodpressure }} || {{ healthparameter.glucose }}  ||  {{ healthparameter.pulse }} ||  {{ healthparameter.measuredOn }}
          </li>
        </ul>
      </div>
    </div>
  </app-layout>
</template>

<script>
Vue.component("healthparameter-profile", {
  template: "#healthparameter-profile",
  data: () => ({
    healthparameter: null,
    noParameterFound: false,
    healthparameters: [],
  }),
  created: function () {
    const healthParamId = this.$javalin.pathParams["healthparameter-id"];
    const url = `/api/healthparameters/${healthParamId}`
    axios.get(url)
        .then(res => this.healthparameter = res.data)
        .catch(error => {
          console.log("No data found for id passed in the path parameter: " + error)
          this.noUserFound = true
        })
  },
  methods: {
    updateHealthParameter: function () {
      const healthParamId = this.$javalin.pathParams["healthparameter-id"];
      const url = `/api/healthparameters/${healthParamId}`
      axios.patch(url,
          {
            bloodpressure: this.healthparameter.bloodpressure,
            glucose: this.healthparameter.glucose,
            pulse: this.healthparameter.pulse
          })
          .then(response =>
              this.healthparameter.push(response.data))
          .catch(error => {
            console.log(error)
          })
      alert("Data updated!")
    },
    deleteHealthParameters: function () {
      if (confirm("Do you really want to delete?")) {
        const healthParamId = this.$javalin.pathParams["healthparameter-id"];
        const url = `/api/healthparameters/${healthParamId}`
        axios.delete(url)
            .then(response => {
              alert("Data deleted")
              //display the /users endpoint
              window.location.href = '/healthparameters';
            })
            .catch(function (error) {
              console.log(error)
            });
      }
    }
  }
});
</script>