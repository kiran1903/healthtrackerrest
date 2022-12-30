<template id="healthparameters-overview">
  <app-layout>
    <div class="card bg-light mb-3">
      <div class="card-header">
        <div class="row">
          <div class="col-6">
            Health Parameters
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
        <form id="addHealthParameters">
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" style="width: 120px;" id="input-healthparameters-userId">User Id</span>
            </div>
            <div class="input-group mb-3">
              <select v-model="formData.userId" name="userId" class="form-control" v-model="healthparameters.userId">
                <option v-for="user in users" :value="user.id">{{user.name}}</option>
              </select>
              <input type="number" class="form-control" v-model="formData.userId" name="userId"  readonly/>
            </div>
            <div class="input-group mb-3">
              <div class="input-group-prepend">
                <span class="input-group-text" style="width: 120px;" id="input-healthparameters-description">Blood Pressure</span>
              </div>
              <input type="text" class="form-control" v-model="formData.bloodpressure" name="bloodpressure" list="HealthParametersList" placeholder="Blood Pressure"/>
            </div>

            <div class="input-group mb-3">
              <div class="input-group-prepend ">
                <span class="input-group-text" style="width: 120px;" id="input-healthparameters-pulse">Pulse</span>
              </div>
              <input type="number" class="form-control" v-model="formData.pulse" placeholder="Pulse" name="pulse">
              &nbsp&nbsp
              <div class="input-group-prepend ">
                <span class="input-group-text" style="width: 120px;" id="input-healthparameters-calories">Glucose</span>
              </div>
              <input type="number" class="form-control" v-model="formData.glucose" placeholder="Glucose" name="glucose">
            </div>
        </form>
        <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link" @click="addHealthParameters()">Add Health Parameters</button>

      </div>

    </div>

    <!-- List Group - displays all the healthparameters -->
    <div class="list-group list-group-flush">
      <div class="list-group-item d-flex align-items-start"
           v-for="(healthparameters,index) in healthparameters" v-bind:key="index"><div class="mr-auto p-2">
        <span><a :href="`/healthparameters/${healthparameters.id}`"> Blood Pressure : {{ healthparameters.bloodPressure }}  ||  Pulse : {{ healthparameters.pulse }}  ||  Glucose : {{ healthparameters.glucose }}  ||  Date : {{ healthparameters.measuredOn }}  ||  User Id : {{ healthparameters.user_id }}</a></span>
      </div>

        <div class="p2">
          <a :href="`/healthparameters/${healthparameters.id}`">
            <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link">
              <i class="fa fa-pencil" aria-hidden="true"></i>
            </button>
          </a>
          <button rel="tooltip" title="Delete" class="btn btn-info btn-simple btn-link"
                  @click="deleteHealthParameters(healthparameters, index)">
            <i class="fas fa-trash" aria-hidden="true"></i>
          </button>
        </div>
      </div>
    </div>
  </app-layout>
</template>

<script>
Vue.component("healthparameters-overview",{
  template: "#healthparameters-overview",
  data: () => ({
    healthparameters: [],
    formData: [],
    hideForm :true
  }),
  created() {
    this.fetchHealthParameters();
    axios.get(`/api/healthparameters`)
        .then(res => this.healthparameters = res.data)
        .catch(() => alert("Error while fetching healthparameters"));
  },
  methods: {
    fetchHealthParameters: function () {
      axios.get("/api/healthparameters")
          .then(res => this.healthparameters = res.data)
          .catch(() => alert("Error while fetching healthparameters"));
    },
    deleteHealthParameters: function (healthparameters, index) {
      if (confirm('Are you sure you want to delete this healthparameters? This action cannot be undone.', 'Warning')) {
        //user confirmed delete
        const healthparametersId = healthparameters.id;
        const url = `/api/healthparameters/${healthparametersId}`;
        axios.delete(url)
            .then(response =>
                //delete from the local state so Vue will reload list automatically
                this.activities.splice(index, 1).push(response.data))
            .catch(function (error) {
              console.log(error)
            });
      }
    },
    addHealthParameters: function (){
      const url = `/api/healthparameters`;
      const timestamp = new Date().toISOString().slice(0, 30);
      axios.post(url,
          {
            bloodpressure: this.formData.bloodpressure,
            pulse: this.formData.pulse,
            glucose: this.formData.glucose,
            userId:this.formData.userId,
            measuredOn:timestamp
          })
          .then(response => {
            this.healthparameters.push(response.data)
            this.hideForm= true;
            this.fetchHealthParameters()
          })
          .catch(error => {
            console.log(error)
          })
    }
  }
});
</script>