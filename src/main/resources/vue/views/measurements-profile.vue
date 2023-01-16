<template id="measurements-profile">
  <app-layout>
    <div v-if="noDataFound">
      <p> We're sorry, we were not able to retrieve this information.</p>
      <p> View <a :href="'/measurements'">all information</a>.</p>
    </div>
    <div class="card bg-light mb-3" v-if="!noDataFound">
      <div class="card-header">
        <div class="row">
          <div class="col-6"> Measurements Profile </div>
          <div class="col" align="right">
            <button rel="tooltip" title="Update"
                    class="btn btn-info btn-simple btn-link"
                    @click="updateMeasurements()">
              <i class="far fa-save" aria-hidden="true"></i>
            </button>
            <button rel="tooltip" title="Delete"
                    class="btn btn-info btn-simple btn-link"
                    @click="deleteMeasurements()">
              <i class="fas fa-trash" aria-hidden="true"></i>
            </button>
          </div>
        </div>
      </div>
      <div class="card-body">
        <form id="addMeasurements">
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-id">User Id</span>
            </div>
            <input type="number" class="form-control" v-model="measurements.user_id" name="User Id" placeholder="User Id"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-weight">Weight</span>
            </div>
            <input type="number" class="form-control" v-model="measurements.weight" name="Weight" placeholder="Weight"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-height">Height</span>
            </div>
            <input type="number" class="form-control" v-model="measurements.height" name="Height" placeholder="Height"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-bmi">BMI</span>
            </div>
            <input type="" class="form-control" v-model="measurements.bmi" name="BMI" placeholder="BMI"/>
          </div>
        </form>
      </div>
      <div class="card-footer text-left">
      </div>
    </div>
  </app-layout>
</template>

<script>
Vue.component("measurements-profile", {
  template: "#measurements-profile",
  data: () => ({
    measurements: null,
    noDataFound: false,
  }),
  created: function () {
    const measurementId = this.$javalin.pathParams["measurement-id"];
    const url = `/api/measurements/${measurementId}`
    axios.get(url)
        .then(res => this.measurements = res.data)
        .catch(error => {
          console.log("No data found in the path parameter: " + error)
          this.noDataFound = true
        })
  },
  methods: {
    updateMeasurements: function () {
      const measurementId = this.$javalin.pathParams["userid"];
      const url = `/api/measurements/${measurementId}`
      axios.patch(url,
          {
            user_id: this.formData.user_id,
            height: this.formData.height,
            weight: this.formData.weight
            //bmi: this.formData.bmi
          })
          .then(response =>
              this.measurements.push(response.data))
          .catch(error => {
            console.log(error)
          })
      alert("Data updated!")
    },
    deleteMeasurements: function () {
      if (confirm("Do you really want to delete?")) {
        const measurementId = this.$javalin.pathParams["userid"];
        const url = `/api/measurements/${measurementId}`
        axios.delete(url)
            .then(response => {
              alert("Data deleted")
              //display the /users endpoint
              window.location.href = '/measurements';
            })
            .catch(function (error) {
              console.log(error)
            });
      }
    }
  }
});
</script>