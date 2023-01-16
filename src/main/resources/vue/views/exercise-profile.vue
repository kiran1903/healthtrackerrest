<template id="exercise-profile">
  <app-layout>
    <div v-if="noDataFound">
      <p> We're sorry, we were not able to retrieve this information.</p>
      <p> View <a :href="'/exercise'">all information</a>.</p>
    </div>
    <div class="card bg-light mb-3" v-if="!noDataFound">
      <div class="card-header">
        <div class="row">
          <div class="col-6"> Exercise Profile </div>
          <div class="col" align="right">
            <button rel="tooltip" title="Update"
                    class="btn btn-info btn-simple btn-link"
                    @click="updateExercise()">
              <i class="far fa-save" aria-hidden="true"></i>
            </button>
            <button rel="tooltip" title="Delete"
                    class="btn btn-info btn-simple btn-link"
                    @click="deleteExercise()">
              <i class="fas fa-trash" aria-hidden="true"></i>
            </button>
          </div>
        </div>
      </div>
      <div class="card-body">
        <form id="addExercise">
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-name">User Id</span>
            </div>
            <input type="" class="form-control" v-model="exercises.user_id" name="User Id" placeholder="User Id"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-date">Date</span>
            </div>
            <input type="date" class="form-control" v-model="exercises.started" name="Date" placeholder="Date"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-day">Day</span>
            </div>
            <input type="text" class="form-control" v-model="exercises.day" name="Day" placeholder="Day"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-exercise">Exercise</span>
            </div>
            <input type="text" class="form-control" v-model="exercises.exercise" name="Exercise" placeholder="Exercise"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-sleep-duration">Duration</span>
            </div>
            <input type="number" class="form-control" v-model="exercises.duration" name="Duration" placeholder="Duration"/>
          </div>
        </form>
      </div>
      <div class="card-footer text-left">
      </div>
    </div>
  </app-layout>
</template>

<script>
Vue.component("exercise-profile", {
  template: "#exercise-profile",
  data: () => ({
    exercises: null,
    noDataFound: false,
  }),
  created: function () {
    const exerciseId = this.$javalin.pathParams["exercise-id"];
    const url = `/api/exercisetracker/${exerciseId}`
    axios.get(url)
        .then(res => this.exercises = res.data)
        .catch(error => {
          console.log("No data found in the path parameter: " + error)
          this.noDataFound = true
        })
  },
  methods: {
    updateExercise: function () {
      const exerciseId = this.$javalin.pathParams["exercise-id"];
      const url = `/api/exercisetracker/${exerciseId}`
      axios.patch(url,
          {
            exercise: this.exercises.exercise,
            day: this.exercises.day,
            duration: this.exercises.duration,
            user_id:this.exercises.user_id
          })
          .then(response =>
              this.exercises.push(response.data))
          .catch(error => {
            console.log(error)
          })
      alert("Data updated!")
    },
    deleteExercise: function () {
      if (confirm("Do you really want to delete?")) {
        const exerciseId = this.$javalin.pathParams["exercise-id"];
        const url = `/api/exercisetracker/${exerciseId}`
        axios.delete(url)
            .then(response => {
              alert("Data deleted")
              //display the /exercise endpoint
              window.location.href = '/exercise';
            })
            .catch(function (error) {
              console.log(error)
            });
      }
    }
  }
});
</script>