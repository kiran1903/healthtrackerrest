<template id="exercise-overview">
  <app-layout>
    <div class="card bg-light mb-3">
      <div class="card-header">
        <div class="row">
          <div class="col-6">
            Exercise Tracker (Gym)
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
        <form id="addExerciseInfo">
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" style="width: 120px;" id="input-exercise-userId">User Id</span>
            </div>
            <div class="input-group mb-3">
              <select v-model="formData.userId" name="userId" class="form-control" v-model="exercise.userId">
                <option v-for="user in users" :value="user.id">{{user.name}}</option>
              </select>
              <input type="number" class="form-control" v-model="formData.userId" name="userId"  readonly/>
            </div>
            <div class="input-group mb-3">
              <div class="input-group-prepend">
                <span class="input-group-text" style="width: 120px;" id="input-exercise-description">Exercise</span>
              </div>
              <input type="text" class="form-control" v-model="formData.bloodpressure" name="bloodpressure" list="HealthParametersList" placeholder="Blood Pressure"/>
            </div>

            <div class="input-group mb-3">
              <div class="input-group-prepend ">
                <span class="input-group-text" style="width: 120px;" id="input-exercise-pulse">Pulse</span>
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

    <!-- List Group - displays all the exercise -->
    <div class="list-group list-group-flush">
      <div class="list-group-item d-flex align-items-start"
           v-for="(exercise,index) in exercise" v-bind:key="index"><div class="mr-auto p-2">
        <span><a :href="`/exercise/${exercise.id}`"> Exercise: {{ exercise.exercise }}, Day : {{ exercise.day }}, Duration : {{ exercise.duration }}, User Id : {{ exercise.user_id }}</a></span>
      </div>

        <div class="p2">
          <a :href="`/exercise/${exercise.id}`">
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
Vue.component("exercise-overview",{
  template: "#exercise-overview",
  data: () => ({
    exercise: [],
    formData: [],
    hideForm :true
  }),
  created() {
    this.fetchExerciseInfo();
    axios.get(`/api/exercisetracker`)
        .then(res => this.exercise = res.data)
        .catch(() => alert("Error while fetching exercise info"));
  },
  methods: {
    fetchExerciseInfo: function () {
      axios.get("/api/exercisetracker")
          .then(res => this.exercise = res.data)
          .catch(() => alert("Error while fetching exercise info"));
    },
    deleteExerciseInfo: function (exerciseInfo, index) {
      if (confirm('Are you sure you want to delete this Exercise Info? This action cannot be undone.', 'Warning')) {
        //user confirmed delete
        const exerciseInfoId = exerciseInfo.id;
        const url = `/api/exercisetracker/${exerciseInfoId}`;
        axios.delete(url)
            .then(response =>
                //delete from the local state so Vue will reload list automatically
                this.exercise.splice(index, 1).push(response.data))
            .catch(function (error) {
              console.log(error)
            });
      }
    },
    addExerciseInfo: function (){
      const url = `/api/exercisetracker`;
      const timestamp = new Date().toISOString().slice(0, 30);
      axios.post(url,
          {
            exercise: this.formData.exercise,
            day: this.formData.day,
            duration: this.formData.duration,
            user_id:this.formData.user_id
          })
          .then(response => {
            this.exercise.push(response.data)
            this.hideForm= true;
            this.fetchExerciseInfo()
          })
          .catch(error => {
            console.log(error)
          })
    }
  }
});
</script>