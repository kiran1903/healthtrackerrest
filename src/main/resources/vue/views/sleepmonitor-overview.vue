<template id="sleepmonitor-overview">
  <app-layout>
    <div class="card bg-light mb-3">
      <div class="card-header">
        <div class="row">
          <div class="col-6">
            Sleep Information
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
        <form id="addSleepInfo">
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-name">User Id</span>
            </div>
            <input type="" class="form-control" v-model="formData.user_id" name="User Id" placeholder="User Id"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-date">Date</span>
            </div>
            <input type="date" class="form-control" v-model="formData.date" name="Date" placeholder="Date"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-day">Day</span>
            </div>
            <input type="text" class="form-control" v-model="formData.day" name="Day" placeholder="Day"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-sleep-duration">Sleep Duration</span>
            </div>
            <input type="" class="form-control" v-model="formData.sleepDuration" name="Sleep Duration" placeholder="Sleep Duration"/>
          </div>
        </form>
        <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link" @click="addSleepInfo()">Add SleepInfo</button>
      </div>
    </div>
    <div class="list-group list-group-flush">
      <div class="list-group-item d-flex align-items-start"
           v-for="(sleepData,index) in sleepmonitor" v-bind:key="index">
        <div class="mr-auto p-2">
          <span><a :href="`/sleepmonitor/${sleepData.id}`"> User ID: {{ sleepData.user_id }} || Date: {{ sleepData.date }} ({{ sleepData.day }}) || Sleep Duration: {{ sleepData.sleepDuration }}</a></span>
        </div>
        <div class="p2">
          <a :href="`/sleepmonitor/${sleepData.id}`">
            <button rel="tooltip" title="Update" class="btn btn-info btn-simple btn-link">
              <i class="fa fa-pencil" aria-hidden="true"></i>
            </button>
          </a>
          <button rel="tooltip" title="Delete" class="btn btn-info btn-simple btn-link"
                  @click="deleteSleepInfo(sleepData, index)">
            <i class="fas fa-trash" aria-hidden="true"></i>
          </button>
        </div>
      </div>
    </div>
  </app-layout>
</template>

<script>
Vue.component("sleepmonitor-overview",{
  template: "#sleepmonitor-overview",
  data: () => ({
    sleepmonitor: [],
    formData: [],
    hideForm :true
  }),
  created() {
    this.fetchSleepMonitorInfo();
  },
  methods: {
    fetchSleepMonitorInfo: function () {
      axios.get("/api/sleepmonitoring")
          .then(res => this.sleepmonitor = res.data)
          .catch(() => alert("Error while fetching sleepmonitoring info"));
    },
    addSleepInfo: function (){
      const url = `/api/sleepmonitoring`;
      axios.post(url,
          {
            user_id: this.formData.user_id,
            date: this.formData.date,
            day: this.formData.day,
            sleepDuration: this.formData.sleepDuration
          })
          .then(response => {
            this.sleepmonitor.push(response.data)
            this.hideForm= true;
          })
          .catch(error => {
            console.log(error)
          })
    },
    deleteSleepInfo: function (sleepData, index) {
      if (confirm('Are you sure you want to delete this information? This action cannot be undone.', 'Warning')) {
        //user confirmed delete
        const sleepInfoId = sleepData.id;
        const url = `/api/sleepmonitoring/${sleepInfoId}`;
        axios.delete(url)
            .then(response =>
                //delete from the local state so Vue will reload list automatically
                this.sleepmonitor.splice(index, 1).push(response.data))
            .catch(function (error) {
              console.log(error)
            });
      }
    }
  }
});
</script>