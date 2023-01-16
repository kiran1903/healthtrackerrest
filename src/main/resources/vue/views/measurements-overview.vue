<template id="measurements-overview">
  <app-layout>
    <div class="card bg-light mb-3">
      <div class="card-header">
        <div class="row">
          <div class="col-6">
            Measurements Information
          </div>
          <div class="col" align="right">
            <button rel="tooltip" title="Add"
                    class="btn btn-info btn-simple btn-link"
                    @click="hideForm =!hideForm">
              <i class="fa fa-plus" aria-hidden="true"></i>
            </button>
          </div>
        </div>
      </div>
      <div class="card-body" :class="{ 'd-none': hideForm}">
        <form id="addMeasurements">
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-id">User Id</span>
            </div>
            <input type="number" class="form-control" v-model="formData.user_id" name="User Id" placeholder="User Id"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-weight">Weight</span>
            </div>
            <input type="number" class="form-control" v-model="formData.weight" name="Weight" placeholder="Weight"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-height">Height</span>
            </div>
            <input type="number" class="form-control" v-model="formData.height" name="Height" placeholder="Height"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-bmi">BMI</span>
            </div>
            <input type="" class="form-control" v-model="formData.bmi" name="BMI" placeholder="BMI"/>
          </div>
        </form>
        <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link" @click="addMeasurements()">Add Measurements</button>
      </div>
    </div>
    <div class="list-group list-group-flush">
      <div class="list-group-item d-flex align-items-start"
           v-for="(measurement,index) in measurements" v-bind:key="index">
        <div class="mr-auto p-2">
          <span><a :href="`/measurements/${measurement.id}`"> User ID: {{ measurement.user_id }} || Weight: {{ measurement.weight }} || Height: {{ measurement.height }}  || BMI: {{ measurement.bmi }}</a></span>
        </div>
        <div class="p2">
          <a :href="`/measurements/${measurement.id}`">
            <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link">
              <i class="fa fa-pencil" aria-hidden="true"></i>
            </button>
          </a>
          <button rel="tooltip" title="Delete" class="btn btn-info btn-simple btn-link"
                  @click="deleteMeasurements(measurement, index)">
            <i class="fas fa-trash" aria-hidden="true"></i>
          </button>
        </div>
      </div>
    </div>
  </app-layout>
</template>

<script>
Vue.component("measurements-overview",{
  template: "#measurements-overview",
  data: () => ({
    measurements: [],
    formData: [],
    hideForm :true
  }),
  created() {
    this.fetchMeasurements();
  },
  methods: {
    fetchMeasurements: function () {
      axios.get("/api/measurements")
          .then(res => this.measurements = res.data)
          .catch(() => alert("Error while fetching info"));
    },
    addMeasurements: function (){
      const url = `/api/measurements`;
      axios.post(url,
          {
            user_id: this.formData.user_id,
            height: this.formData.height,
            weight: this.formData.weight
            //bmi: this.formData.bmi
          })
          .then(response => {
            this.measurements.push(response.data)
            this.hideForm= true;
          })
          .catch(error => {
            console.log(error)
          })
    },
    deleteMeasurements: function (measurement, index) {
      if (confirm('Are you sure you want to delete this information? This action cannot be undone.', 'Warning')) {
        //user confirmed delete
        const measurementId = measurement.id;
        const url = `/api/measurements/${measurementId}`;
        axios.delete(url)
            .then(response =>
                //delete from the local state so Vue will reload list automatically
                this.measurements.splice(index, 1).push(response.data))
            .catch(function (error) {
              console.log(error)
            });
      }
    }
  }
});
</script>